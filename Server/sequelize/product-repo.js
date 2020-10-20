const Sequelize = require('sequelize')
const ProductModel = require('./models/product')
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
    storage: 'database/database.sqlite'
})

const Product = ProductModel(sequelize, Sequelize);
const User = UserModel(sequelize, Sequelize);

async function getAllProducts() {
    return Product.findAll();
};

async function getAllUsers() {
    return User.findAll();
};

module.exports = {
    getAllProducts,
    getAllUsers
}