const express = require('express');
const cookieParser = require('cookie-parser');
const dotenv = require('dotenv');
const mysql = require('mysql2');
const app = express();
const port = 8000;
dotenv.config({ path: './.env' });
const connection = mysql.createConnection({
    host: process.env.DATABASE_HOST,
    user: process.env.DATABASE_USER,
    password: process.env.DATABASE_PASSWORD,
    database: process.env.DATABASE,
    port: 3306
});
connection.connect((err) => {
    if (err) {
        throw err;
    } else {
        console.log('Connected to the database');
    }
});
app.use(express.static('public'));
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());
app.set('view engine', 'ejs');
// Define routers

const signinRouter = require('./routers/signin');
const signupRouter = require('./routers/signup');
const composeRouter = require('./routers/compose');

// authentication user
const isAuthenticated = (req, res, next) => {
    const email = req.cookies.email;
    const password = req.cookies.password;

    if (email && password) {
        // User is authenticated, proceed to the next middleware or route
        next();
    } else {
        // User is not authenticated, show a 403 error and 'access denied' page
        res.status(403).render('access-denied');
    }
};
// use isAuthenticated
app.use('/inbox', isAuthenticated);
app.use('/outbox', isAuthenticated);
app.use('/compose', isAuthenticated);
app.use('/signup/inbox', isAuthenticated);
app.use('/signup/outbox', isAuthenticated);
app.use('/signup/compose', isAuthenticated);

// app.post('/deleteEmail', (req, res) => {
//     const userEmail = req.cookies.email;
//     const emailIdToDelete = req.body.emailId;

//     // Delete the email only for the current user
//     connection.query('DELETE FROM messages WHERE message_id = ? AND (sender_id = (SELECT id FROM users WHERE email = ?) OR message_id IN (SELECT messages_id FROM message_recipient WHERE message_recipient_id = (SELECT id FROM users WHERE email = ?)))', [emailIdToDelete, userEmail, userEmail], (err, results) => {
//         if (err) {
//             console.error(err);
//             res.status(500).json({ success: false, error: 'Internal Server Error' });
//             return;
//         }

//         // Send a success response
//         res.json({ success: true });
//     });
// });
// sign in, sign up
app.get('/', (req, res) => {
    res.render('signin');
});
app.post('/', signinRouter);
app.get('/signup', (req, res) => {
    res.render('signup');
});
app.post('/signup', signupRouter);

// compose
app.post('/send-email', composeRouter);
app.get('/compose', (req, res) => {
    res.render('compose');
});
//outbox
app.get('/outbox', (req, res) => {
    const userEmail = req.cookies.email;
    const itemsPerPage = 5; // Set the desired number of items per page
    const currentPage = parseInt(req.query.page) || 1;
    connection.query('SELECT name FROM users WHERE email = ?', [userEmail], (err, userResults) => {
        if (err) {
            console.error(err);
            res.status(500).send('Internal Server Error');
            return;
        }
        if (userResults.length === 0) {
            // Handle the case where no user is found for the provided email
            res.status(404).send('User not found');
            return;
        }
        const userName = userResults[0].name;
        connection.query('SELECT ' +
            'm.body, u_recipient.name AS recipient_name, ' +
            'm.subject, ' +
            'm.date_time AS time ' +
            'FROM ' +
            'messages m ' +
            'INNER JOIN ' +
            'users u ON m.sender_id = u.id ' +
            'INNER JOIN ' +
            'message_recipient mr ON m.message_id = mr.messages_id ' +
            'INNER JOIN ' +
            'users u_recipient ON mr.message_recipient_id = u_recipient.id ' +
            'WHERE ' +
            'u.email = ?' +
            'ORDER BY date_time DESC', [userEmail], (err, outboxresults) => {
                if (err) {
                    console.error(err);
                    res.status(500).send('Internal Server Error');
                    return;
                }
                const totalItems = outboxresults.length;
                const totalPages1 = Math.ceil(totalItems / itemsPerPage);
                const startIndex = (currentPage - 1) * itemsPerPage;
                const endIndex = startIndex + itemsPerPage;
                const outboxData = outboxresults.slice(startIndex, endIndex);
                res.render('outbox', { userName, outboxData, totalPages1 });
            });
    });
});
// inbox 
app.get('/inbox', (req, res) => {
    const userEmail = req.cookies.email;
    const itemsPerPage = 5; // Set the desired number of items per page
    const currentPage = parseInt(req.query.page) || 1;
    connection.query('SELECT name FROM users WHERE email = ?', [userEmail], (err, userResults) => {
        if (err) {
            console.error(err);
            res.status(500).send('Internal Server Error');
            return;
        }
        if (userResults.length === 0) {
            // Handle the case where no user is found for the provided email
            res.status(404).send('User not found');
            return;
        }
        const userName = userResults[0].name;
        connection.query('SELECT ' +
            'm.body, u_sender.name AS sender_name, ' +
            'm.subject, ' +
            'm.date_time AS time ' +
            'FROM ' +
            'users u_sender ' +
            'INNER JOIN ' +
            'messages m ON u_sender.id = m.sender_id ' +
            'INNER JOIN ' +
            'message_recipient mr ON m.message_id = mr.messages_id ' +
            'INNER JOIN ' +
            'users u_recipient ON u_recipient.id = mr.message_recipient_id ' +
            'WHERE ' +
            'u_recipient.email = ?' +
            'ORDER BY date_time DESC', [userEmail], (err, inboxresults) => {
                if (err) {
                    console.error(err);
                    res.status(500).send('Internal Server Error');
                    return;
                }
                const totalItems = inboxresults.length;
                const totalPages = Math.ceil(totalItems / itemsPerPage);
                const startIndex = (currentPage - 1) * itemsPerPage;
                const endIndex = startIndex + itemsPerPage;
                const inboxData = inboxresults.slice(startIndex, endIndex);
                res.render('inbox', { userName, inboxData, totalPages });
            });
    });
});
app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
});
