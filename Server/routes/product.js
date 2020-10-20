const { Router } = require('express');
var express = require('express');
var router = express.Router();

var productRepo = require('../sequelize/product-repo');

router.get('/', async function(req, res, next) {
    const products = await productRepo.getAllProducts();
    res.json(products);
});

module.exports = router;