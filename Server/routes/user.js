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

router.get('/total/:id', async function(req, res, next) {
  const user = await productRepo.getUserById(req.params.id);
  const total_spent = user.total_spent;
  res.json({total_spent});
});

router.post('/transaction/:id', async function(req, res, next) {
  const transaction = await productRepo.getTransaction(req.params.id);
  res.json(transaction);
});

router.post('/checkout', async function(req, res, next) {
  await productRepo.checkout(req, res);
});

router.get('/:username', async function(req, res, next) {
  const user = await productRepo.getUserByUsername(req.params.username);
  res.json({user});
});

router.get('/id/:id', async function(req, res, next) {
  const user = await productRepo.getUserById(req.params.id);
  const name = user.name;
  res.json({name});
});

router.post('/cert', async function(req, res, next) {
  const uuid = productRepo.saveCert(req.body);
  res.json({uuid});
});

module.exports = router;