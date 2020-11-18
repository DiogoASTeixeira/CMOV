const Sequelize = require('sequelize')
const ProductModel = require('../sequelize/models/product')
const UserModel = require('../sequelize/models/user')
const TransactionProductModel = require('../sequelize/models/transaction_product')
const TransactionModel = require('../sequelize/models/transaction')
const VoucherModel = require('../sequelize/models/voucher')

const sequelize = new Sequelize('codementor', 'root', 'root', {
    host: 'localhost',
    dialect: 'sqlite',
    pool: {
      max: 10,
      min: 1,
      acquire: 30000,
      idle: 10000
    },
    storage: 'database/database.sqlite'
})

const User = UserModel(sequelize, Sequelize)
const Transaction = TransactionModel(sequelize, Sequelize)
const Voucher = VoucherModel(sequelize, Sequelize);
const Product = ProductModel(sequelize, Sequelize);
const TransactionProduct = TransactionProductModel(sequelize, Sequelize);

User.hasMany(Transaction);
Transaction.belongsTo(User);

User.hasMany(Voucher);
Voucher.belongsTo(User);
Voucher.belongsTo(Transaction);

//get all products
async function getAllProducts() {
    return Product.findAll();
};

//get specific product
async function getProduct(id) {
    return Product.findOne({ where: {id: id}});
}

//get all products on a transaction
async function getTransactionProducts(body){
    let query = "SELECT p.id, p.name, p.value, tp.count FROM Products p INNER JOIN TransactionProducts tp on tp.ProductId = p.id WHERE tp.TransactionId = :id";
    return sequelize.query(query, { replacements: { id: body.TransactionId } }).catch(function(err) {
        console.log(err);
    });
}

//get all users
async function getAllUsers() {
    return User.findAll();
};

//get specified user by id
async function getUserByUsername(name) {
    return User.findOne({ where: {username: name}});
}

//register new user on database
async function registerUser(info) {
    info.id = create_UUID();
    
    return User.create(info).catch(function(err) {
        console.log(err);
    });
}

//login
async function login(email, password) {
    return User.findOne({ where: {email: email, password: password}}).catch(function(err) {
        console.log(err);
    });
}

//get user unused vouchers
async function getUnusedVouchers(info){
    return Voucher.findAll({ where: {UserId: info[0].UserId, used: false}}).catch(function(err) {
        console.log(err);
    });
}

//get all transactions from an user
async function getTransactions(info){
    return Transaction.findAll({ where: {UserId: info[0].UserId}}).catch(function(err) {
        console.log(err);
    });
}

//get a transaction from an user
async function getTransaction(id){
    return Transaction.findOne({ where: {id: id}}).catch(function(err) {
        console.log(err);
    });
}

//checkout
async function checkout(req, res) {
    let transaction = {
        id: create_UUID_small(),
        UserId: req.body.UserId
    };

    let total_spent = 0;
    let coffee_price = 0;
    let coffee = false;
    let productsDB;
    let usedProducts = new Map();
    let coffee_number = 0;

    Product.findAll().then(products => {
        productsDB = products;

        req.body.products.forEach(product => {
            for(let i = 0; i < productsDB.length; i++){
                if(product.id == productsDB[i].id){
                    if(productsDB[i].name == "Coffee"){
                        coffee = true;
                        coffee_price = productsDB[i].value;
                        coffee_number++;
                    };

                    total_spent = total_spent + (productsDB[i].value * product.count);
                }
            }
        });

        Voucher.findOne({where: {id: req.body.voucher.id}}).then(voucher => {
            transaction.voucher = voucher;
            
            if (transaction.voucher != null && !transaction.voucher.used && !transaction.voucher.coffee && transaction.voucher.UserId == req.body.UserId) {
                transaction.total_value = total_spent - total_spent * 0.05;
                transaction.discount = 0.05 * total_spent;
            } else if (transaction.voucher != null && !transaction.voucher.used && transaction.voucher.coffee) {
                if (coffee) {
                    transaction.total_value = total_spent - coffee_price;
                    transaction.discount = coffee_price;
                }
            } else {
                transaction.total_value = total_spent;
                transaction.discount = 0;
            }

            Transaction.create(transaction)
                .then(createdTransaction => {
                    req.body.products.forEach(product => {
                        usedProducts.set(product.id, product.count);
                    });
                    usedProducts.forEach(function(count, id) {
                        let trans_prod = {
                            id: create_UUID_small(),
                            ProductId: id,
                            TransactionId: createdTransaction.id,
                            count: count
                        };
                        TransactionProduct.create(trans_prod);
                    });
                    // Update voucher if used
                    if (req.body.voucher != null) {
                        Voucher.update({
                            used: true,
                            TransactionId: transaction.id
                        }, {
                            where: {
                                id: req.body.voucher.id
                            },
                            returning: true, // needed for affectedRows to be populated
                        });
                    }
                    User.findOne({
                            where: {
                                id: req.body.UserId
                            }
                        })
                        .then(user => {
                            let query = "UPDATE Users SET total_spent = total_spent + :total, total_coffees = total_coffees + :coffee_number WHERE id = :id";
                            sequelize.query(query, {
                                    replacements: {
                                        total: createdTransaction.total_value,
                                        id: user.id,
                                        coffee_number: coffee_number
                                    }
                                })
                                .then(([results, metadata]) => {
                                    let user = User.findOne({
                                        where: {
                                            id: req.body.UserId
                                        }
                                    }).then(user => {
                                        //give voucher if more than 3 coffees
                                        if(user.total_coffees >= 3) {
                                            let voucher = {
                                                id: create_UUID(),
                                                used: false,
                                                coffee: true,
                                                UserId: req.body.UserId,
                                                TransactionId: null
                                            }

                                            Voucher.create(voucher);

                                            let query = "UPDATE Users SET total_coffees = :total_coffees - 3 WHERE id = :id";
                                            sequelize.query(query, {
                                                replacements: {
                                                    id: user.id,
                                                    total_coffees: user.total_coffees
                                                }
                                            })
                                        }

                                        //give voucher if another 100 multiple has been reached
                                        if((user.total_spent / 100) > (user.hundred_multiples + 1)) {
                                            let voucher = {
                                                id: create_UUID(),
                                                used: false,
                                                coffee: false,
                                                UserId: req.body.UserId,
                                                TransactionId: null
                                            }

                                            Voucher.create(voucher);

                                            let query = "UPDATE Users SET hundred_multiples = hundred_multiples + 1 WHERE id = :id";
                                            sequelize.query(query, {
                                                replacements: {
                                                    id: user.id,
                                                }
                                            })
                                        }
                                    }).catch(function(err) {
                                        console.log(err);
                                    });
                                }).catch(function(err) {
                                    console.log(err);
                                });
                        }).catch(function(err) {
                            console.log(err);
                        });
                }).catch(function(err) {
                    console.log(err);
                });

                let response = {
                    status: "success",
                    total_spent: transaction.total_value,
                    voucher: transaction.voucher
                };

                res.json(response);

            }).catch(function(err) {
                console.log(err);
            });
        })
        .catch(function(err) {
            console.log(err);
        });
}

function create_UUID_small(){
    var dt = new Date().getTime();
    var uuid = 'xxyxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function create_UUID(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
  }

module.exports = {
    getAllProducts,
    getProduct,
    getTransactionProducts,
    getAllUsers,
    getUserByUsername,
    create_UUID,
    create_UUID_small,
    registerUser,
    login,
    getUnusedVouchers,
    getTransactions,
    getTransaction,
    checkout
}