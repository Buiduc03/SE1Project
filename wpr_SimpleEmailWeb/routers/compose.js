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
router.post('/send-email', (req, res) => {
    const userEmail = req.cookies.email;
    const { name, subject, email, body } = req.body;
    let successMsg = undefined;
    // let mistakes = {};
    // check username 
    connection.query('SELECT * FROM users WHERE name = ?', [name], (err, users) => {
        if (err) {
            console.error(err);
            res.status(500).send('Internal Server Error');
            return;
        }
        if (users.length === 0) {
            return res.status(400).send('Username not found!<br/><a href="/compose">Back</a>');
        }
        // Get sender_id from the user's email
        connection.query('SELECT id FROM users WHERE email = ?', [userEmail], (err, senderResults) => {
            if (err) {
                console.error(err);
                return res.status(500).send('Internal Server Error');
            }

            const senderId = senderResults[0].id;

            // Get recipient_id from the selected recipient's email
            connection.query('SELECT id FROM users WHERE email = ?', [email], (err, recipientResults) => {
                if (err) {
                    console.error(err);
                    return res.status(500).send('Internal Server Error');
                }

                if (recipientResults.length === 0) {
                    return res.status(400).send('Email not found!<br/><a href="/compose">Back</a>');
                }

                const recipientId = recipientResults[0].id;

                // Insert the message into the messages table
                connection.query('INSERT INTO messages (sender_id, subject, body) VALUES (?, ?, ?)', [senderId, subject, body], (err, messageIdResults) => {
                    if (err) {
                        console.error(err);
                        return res.status(500).send('Internal Server Error');
                    }
                    const messageId = messageIdResults.insertId;

                    // Insert the message_recipient entry
                    connection.query('INSERT INTO message_recipient (messages_id, message_recipient_id) VALUES (?, ?)', [messageId, recipientId], (err) => {
                        if (err) {
                            console.error(err);
                            return res.status(500).send('Internal Server Error');
                        }

                        // Send a success response
                        successMsg = "Email sent successfully!";
                        res.render('compose', {
                            msg: successMsg
                        });
                    });
                });
            });
        });
    });
});
module.exports = router;