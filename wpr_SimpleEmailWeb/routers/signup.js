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
}).promise();

router.post('/signup', async (req, res) => {
    // get inputs
    let name = req.body.name;
    let email = req.body.email;
    let password1 = req.body.password1;
    let password2 = req.body.password2;
    // validate inputs
    let successMsg = undefined;
    const mistakes = {};
    if (name === undefined || name === '') {
        mistakes.name = "Please enter your name!";
    }
    // show error messages when user makes any mistake
    // prevent user from entering empty name, password...
    if (email === undefined || email === '') {
        mistakes.email = "Please enter email!";
    } else {
        // prevent duplicated user registration
        let [rows] = await connection.query("SELECT * FROM users WHERE email LIKE ?", email);
        if (rows.length > 0) {
            mistakes.email = "The email address is already used!";
        }
    }
    // prevent short password
    if (password1 === undefined || password1.length < 6) {
        mistakes.password1 = "The password is too short (less than 6 characters)!";
    }
    // make sure user re-enter the password correctly
    if (password2 !== password1) {
        mistakes.password2 = "Re-enter password does not match password!";
    }
    // if there's no mistake, insert user to database
    if (Object.keys(mistakes).length === 0) {
        await connection.query("INSERT INTO users (name, email, password) VALUES (?, ?, ?)", [name, email, password1]);
        successMsg = "Signup successfully!";
    }
    res.render('signup', {
        msg: successMsg,
        err: mistakes,
        params: req.body
    });
});
module.exports = router;