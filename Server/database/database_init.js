const Sequelize = require('sequelize')
const ProductModel = require('../sequelize/models/product')
const UserModel = require('../sequelize/models/user')
const TransactionProductModel = require('../sequelize/models/transaction_product')
const TransactionModel = require('../sequelize/models/transaction')
const VoucherModel = require('../sequelize/models/voucher')
const CertificatesModel = require('../sequelize/models/certificates')
const QrlistModel = require('../sequelize/models/qrlist')

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
const Certificate = CertificatesModel(sequelize, Sequelize);
const Qrlist = QrlistModel(sequelize, Sequelize);

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
      {id: "32bf576f-1d83-4141-9009-8d4c6435d10e", email: "diogo@gmail.com", username: "Diogo", name: "Diogo Teixeira", password:"jxRlcHQCMoGKrhyVysaZIpIlTaslft+K8iLxM227tbY=", card_number:"123124", card_cvs: "102", total_spent: 182, hundred_multiples: 1, nif: "456", total_coffees: 2},
    ])
  })
  .then(() => {
    Transaction.bulkCreate([
      {id: "82d7e", voucher: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ad", total_value: 152, discount: 8, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", orderId: 0},
      {id: "82d7f", total_value: 30, discount: 0, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", orderId: 1},
    ])
  })
    .then(() => {
      Product.bulkCreate([
        {id: "1f45a", name: "Coffee", value: 20.00, icon_path: "coffee.png"},
        {id: "6e89r", name: "Sandwich", value: 30.00, icon_path: "sandwich.png"},
        {id: "1h52w", name: "Milk", value: 40.00, icon_path: "milk.png"},
        {id: "1a25w", name: "Orange Juice", value: 50.00, icon_path: "orange_juice.png"},
      ])
    })
    .then(() => {
      Voucher.bulkCreate([
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ac", used:false, coffee: "false", UserId:"b0e76929-9762-45b7-be1f-2f37d2edf33c", TransactionId:null},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ad", used:true, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:"82d7e"},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935af", used:false, coffee: "true", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ae", used:false, coffee: "true", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ag", used:false, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ah", used:false, coffee: "false", UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},
      ])
    })
      .then(() => {
        TransactionProduct.bulkCreate([
          {id: "7a9af", ProductId: "6e89r", TransactionId:"82d7f", count: 1},
          {id: "2a8ac", ProductId: "1f45a", TransactionId:"82d7e", count: 2},
          {id: "835ac", ProductId: "6e89r", TransactionId:"82d7e", count: 4},
        ])
      })
  .catch(error => {
    console.log("error", error);
  })