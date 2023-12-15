const mysql = require('mysql2');
const dotenv = require('dotenv');
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
// // Create users table
const createUsersTable = `
  CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL
  )
`;
connection.query(createUsersTable, function (err) {
    if (err) throw err;
    console.log("UsersTable created");
});
const users = [
    { id: '001', name: 'User1', email: 'a@a.com', password: '1' },
    { id: '002', name: 'User2', email: 'b@b.com', password: '2' },
    { id: '003', name: 'User3', email: 'c@c.com', password: '3' },
];

const usersInsert = 'INSERT INTO users (id, name, email, password) VALUES ?';
connection.query(usersInsert, [users.map(user => [user.id, user.name, user.email, user.password])], (err, results) => {
    if (err) throw err;
    console.log(`${results.affectedRows} users inserted`);
});
// // Create messages table
const createMessageTable = `
CREATE TABLE IF NOT EXISTS messages (
  message_id INT AUTO_INCREMENT PRIMARY KEY,
  sender_id INT(10),
  subject TEXT,
  body TEXT,
  date_time DATETIME NOT NULL DEFAULT NOW(),
  FOREIGN KEY (sender_id)
    REFERENCES users(id)
);
`;
connection.query(createMessageTable, function (err) {
    if (err) throw err;
    console.log("MessagesTable created");
});
const messages = [
    { message_id: '001', sender_id: '001', subject: 'HelloUser2', body: 'Can you help me with my homework?', date_time: '2023/11/20' },
    { message_id: '002', sender_id: '001', subject: 'HiUser3', body: 'Fix my lap please!', date_time: '2023/11/22' },
    { message_id: '003', sender_id: '002', subject: 'Dear, User1', body: 'I want meet u at...', date_time: '2023/11/23' },
    { message_id: '004', sender_id: '003', subject: 'Dear, User2', body: 'Do you have free time now?', date_time: '2023/11/20' },
    { message_id: '005', sender_id: '003', subject: 'Dear, User1', body: 'Yes, of course!', date_time: '2023/11/23' },
    { message_id: '006', sender_id: '002', subject: 'Dear, User3', body: 'Yes but whats your problem?', date_time: '2023/11/24' },
    { message_id: '007', sender_id: '003', subject: 'Dear, User2', body: 'Nothing', date_time: '2023/11/23' },
    { message_id: '008', sender_id: '001', subject: 'Dear, User2', body: 'Thank you!', date_time: '2023/11/22' }
];
const messagesInsert = 'INSERT INTO messages (message_id, sender_id, subject, body,date_time) VALUES ?';
connection.query(messagesInsert, [messages.map(message => [message.message_id, message.sender_id, message.subject, message.body, message.date_time])], (err, results) => {
    if (err) throw err;
    console.log(`${results.affectedRows} users inserted`);
});
// // Create message_recipient
const createMessage_RecipientTable = `
CREATE TABLE IF NOT EXISTS message_recipient (
  id INT AUTO_INCREMENT PRIMARY KEY,
  messages_id INT,
  message_recipient_id INT,
  FOREIGN KEY (messages_id)
    REFERENCES messages(message_id),
    FOREIGN KEY (message_recipient_id)
    REFERENCES users(id)
);
`;
connection.query(createMessage_RecipientTable, function (err) {
    if (err) throw err;
    console.log("Message_RecipientTable created");
});
const message_recipient = [
    { id: '001', messages_id: '1', message_recipient_id: '002' },
    { id: '002', messages_id: '2', message_recipient_id: '003' },
    { id: '003', messages_id: '3', message_recipient_id: '001' },
    { id: '004', messages_id: '4', message_recipient_id: '002' },
    { id: '005', messages_id: '5', message_recipient_id: '001' },
    { id: '006', messages_id: '6', message_recipient_id: '003' },
    { id: '007', messages_id: '7', message_recipient_id: '002' },
    { id: '008', messages_id: '8', message_recipient_id: '002' }
];
const message_recipientInsert = 'INSERT INTO message_recipient (id, messages_id, message_recipient_id) VALUES ?';
connection.query(message_recipientInsert, [message_recipient.map(message_recipient => [message_recipient.id, message_recipient.messages_id, message_recipient.message_recipient_id])], (err, results) => {
    if (err) throw err;
    console.log(`${results.affectedRows} message_recipient inserted`);
});
connection.end();








