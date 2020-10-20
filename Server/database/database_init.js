const Sequelize = require('sequelize')
const ProductModel = require('../sequelize/models/product')
const UserModel = require('../sequelize/models/user')

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

const Product = ProductModel(sequelize, Sequelize);
const User = UserModel(sequelize, Sequelize);

sequelize
.sync({force:true})
  .then(() => {
    User.bulkCreate([
      {id: "b0e76929-9762-45b7-be1f-2f37d2edf33c", username: "John", name: "John Cena", password:"coiso", card_number:"123123", card_cvs: "101", total_spent: 0, stored_discount:0, nif: "134"},
      {id: "32bf576f-1d83-4141-9009-8d4c6435d10e", username: "Kimbolas", name: "Ricardo Lopes", password:"coiso", card_number:"123123", card_cvs: "101", total_spent: 250, stored_discount:40, nif: "456"},
    ])
  })
  /*.then(() => {
    Transaction.bulkCreate([
      {id: "82d7e51b-9b63-4570-bf32-da837ec09981", voucher: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ac", total_value: 220, discount: 10, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e"},
    ])
  })*/
    .then(() => {
      Product.bulkCreate([
        {id: "d93402fb-8af4-40e0-8c0d-7a05485405f3", name: "Coffee", value: 2, icon_path: "../icons/coffee.png"},
        {id: "c19817fe-2b3b-4c48-877e-7ea98f081e74", name: "Sandwich", value: 3, icon_path: "../icons/sandwich.png"},
        {id: "a807813a-70a6-45d1-b27f-42a406ff2321", name: "Milk", value: 2, icon_path: "../icons/milk.png"},
        {id: "6b6e76f5-85cf-4b65-9697-979524ae0c19", name: "Orange Juice", value: 5, icon_path: "../icons/orange_juice.png"},
      ])
    })/*
    .then(() => {
      Voucher.bulkCreate([
        {id: "b1a3k3v1-cte1-4ab3-b1ce-67cf4d3935ac", used:true, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981"},
        {id: "b1a3k3v1-cfd0-4ab3-b1ce-67cf4d3935ac", used:false, UserId:"32bf576f-1d83-4141-9009-8d4c6435d10e", TransactionId:null},

      ])
    })
      .then(() => {
        TransactionProduct.bulkCreate([
          {ProductId: "d93402fb-8af4-40e0-8c0d-7a05485405f3", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981", count: 1},
          {ProductId: "5c3d170b-ecc3-4fa2-83a9-651d6110f571", TransactionId:"82d7e51b-9b63-4570-bf32-da837ec09981", count: 2},
        ])
        })*/
  .catch(error => {
    console.log("error", error);
  })