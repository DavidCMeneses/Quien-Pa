const { Pool } = require('pg');
require('dotenv').config();

// Connect to the supabase database

const pool = new Pool({
    connectionString: process.env.DATABASE_URL_IPV4,
    ssl: {
        rejectUnauthorized: false
    }
});

module.exports = pool;