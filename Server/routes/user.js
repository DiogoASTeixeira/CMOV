var express = require('express');
var router = express.Router();

var productRepo = require('../sequelize/repo');

router.get('/', async function(req, res, next) {
  const users = await productRepo.getAllUsers();
  res.json({users});
});

router.post('/register', async function(req, res, next) {
  const user = await productRepo.registerUser(req.body);
  res.json({user});
})

router.post('/login', async function(req, res, next) {
  const user = await productRepo.login(req.body.email, req.body.password);
  res.json({user});
});

router.post('/voucher', async function(req, res, next) {
  const voucher = await productRepo.getUnusedVouchers(req.body);
  res.json(voucher);
});

router.post('/transaction', async function(req, res, next) {
  const transactions = await productRepo.getTransactions(req.body);
  res.json(transactions);
});

router.post('/transaction/:id', async function(req, res, next) {
  const transaction = await productRepo.getTransaction(req.params.id);
  res.json(transaction);
});

router.post('/checkout', async function(req, res, next) {
  await productRepo.checkout(req);
  res.send("Success");
});

router.get('/:username', async function(req, res, next) {
  const user = await productRepo.getUserByUsername(req.params.username);
  res.json({user});
});

module.exports = router;