const { Router } = require('express');
var express = require('express');
var router = express.Router();

var productRepo = require('../sequelize/repo');

router.get('/', async function(req, res, next) {
    const products = await productRepo.getAllProducts();
    res.json(products);
});

router.get('/:id', async function(req, res, next) {
    const products = await productRepo.getProduct(req.params.id);
    res.json(products);
});

router.post('/transaction', async function(req, res, next) {
    const products = await productRepo.getTransactionProducts(req.body[0]);
    res.json(products[0]);
})

router.delete('/transaction/:id', async function(req, res, next) {
    const deleted = await productRepo.deleteTransaction(req.params.id);
    res.json({deleted});
})

module.exports = router;