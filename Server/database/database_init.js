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
    storage: './database/database.sqlite'
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

sequelize
.sync({force:true})
  .then(() => {
    User.bulkCreate([
      {id: "b0e76929-9762-45b7-be1f-2f37d2edf33c", email: "fabio@gmail.com", username: "Fabio", name: "Fábio Azevedo", password:"4sPYOZOhJo3uODnnDZhuyh0hLWJ1iL57XWpj5TRESNY=", card_number:"123123", card_cvs: "101", total_spent: 0, hundred_multiples: 0, nif: "134", total_coffees: 0},
      {id: "32bf576f-1d83-4141-9009-8d4c6435d10e", email: "diogo@gmail.com", username: "Diogo", name: "Diogo Teixeira", password:"jxRlcHQCMoGKrhyVysaZIpIlTaslft+K8iLxM227tbY=", card_number:"123124", card_cvs: "102", total_spent: 250, hundred_multiples: 2, nif: "456", total_coffees: 0},
    ])
  })
  .then(() => {
    Transaction.bulkCreate([
      {id: "82d7e51b-9b63-4570-bf32-da837ec09981", voucher: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ac", total_value: 220, discount: 11, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e"},
      {id: "82d7e51b-9b63-4570-bf32-da837ec09982", total_value: 30, discount: 0, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e"}
    ])
  })
    .then(() => {
      Product.bulkCreate([
        {id: "d93402fb-8af4-40e0-8c0d-7a05485405f3", name: "Coffee", value: 20.00, icon_path: "coffee.png"},
        {id: "c19817fe-2b3b-4c48-877e-7ea98f081e74", name: "Sandwich", value: 30.00, icon_path: "sandwich.png"},
        {id: "a807813a-70a6-45d1-b27f-42a406ff2321", name: "Milk", value: 40.00, icon_path: "milk.png"},
        {id: "6b6e76f5-85cf-4b65-9697-979524ae0c19", name: "Orange Juice", value: 50.00, icon_path: "orange_juice.jpg"},
      ])
    })
    .then(() => {
      Voucher.bulkCreate([
        {id: "b1a3k3v1-cte0-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "false", UserId:"b0e76929-9762-45b7-be1f-2f37d2edf33c", TransactionId:null},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ac", used:true, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981"},
        {id: "b1a3k3v1-cfd2-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "true", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cfd3-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "true", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cte4-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cte5-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
      ])
    })
      .then(() => {
        TransactionProduct.bulkCreate([
          {ProductId: "d93402fb-8af4-40e0-8c0d-7a05485405f3", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981", count: 1},
          {ProductId: "6b6e76f5-85cf-4b65-9697-979524ae0c19", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981", count: 4},
          {ProductId: "c19817fe-2b3b-4c48-877e-7ea98f081e74", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09982", count: 1}
        ])
        })
  .catch(error => {
    console.log("error", error);
  })