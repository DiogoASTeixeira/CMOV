var express = require('express');
var router = express.Router();

var productRepo = require('../sequelize/product-repo');

/* GET users listing. */
router.get('/', async function(req, res, next) {
  const users = await productRepo.getAllUsers();
  res.send(users);
});

module.exports = router;