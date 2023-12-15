const express = require('express');
const router = express.Router();
const dotenv = require('dotenv');
const mysql = require('mysql2');
dotenv.config({ path: './.env' });
const connection = mysql.createConnection({
  host: process.env.DATABASE_HOST,
  user: process.env.DATABASE_USER,
  password: process.env.DATABASE_PASSWORD,
  database: process.env.DATABASE,
  port: 3306
});
router.post('/', (req, res) => {
  const email = req.body.email;
  const password = req.body.password;
  let mistakes = {};
  // Check the database for the user
  const query = 'SELECT * FROM users WHERE email = ? AND password = ?';

  connection.query(query, [email, password], (err, results) => {
    if (err) throw err;
    if (results.length > 0) {
      res.cookie('email', email, {
        maxAge: 60000
      });
      res.cookie('password', password, {
        maxAge: 60000
      });
      res.redirect('/inbox');
    } else {
      mistakes.email = "Incorrect email or password!";
      mistakes.password = "Incorrect email or password!";
      res.render('signin', {
        err: mistakes
      });
    }
  });
});
module.exports = router;