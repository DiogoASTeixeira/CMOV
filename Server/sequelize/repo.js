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
    let query = "SELECT p.name, p.value, tp.count FROM Products p INNER JOIN TransactionProducts tp on tp.ProductId = p.id WHERE tp.TransactionId = :id";
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
    return User.create(info).catch(function(err) {
        console.log(err);
    });
}

//login
async function login(username, password) {
    return User.findOne({ where: {username: username, password: password}}).catch(function(err) {
        console.log(err);
    });
}

//get user unused vouchers
async function getUnusedVouchers(info){
    return Voucher.findAll({ where: {UserId: info.UserId, used: false}}).catch(function(err) {
        console.log(err);
    });
}

//get all transactions from an user
async function getTransactions(info){
    return Transaction.findAll({ where: {UserId: info.UserId}}).catch(function(err) {
        console.log(err);
    });
}

//get a transaction from an user
async function getTransaction(info, id){
    return Transaction.findOne({ where: {UserId: info.UserId, id: id}}).catch(function(err) {
        console.log(err);
    });
}

//checkout
async function checkout(body) {
    return;
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
    registerUser,
    login,
    getUnusedVouchers,
    getTransactions,
    getTransaction,
    checkout
}