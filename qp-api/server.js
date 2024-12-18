const express = require('express');
const bodyParser = require('body-parser');
const userRoutes = require('./routes/users');
const activityRoutes = require('./routes/activities');
const categoryRoutes = require('./routes/categories');
const reviewRoutes = require('./routes/reviews');

require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());

app.use('/users', userRoutes);
app.use('/activities', activityRoutes);
app.use('/categories', categoryRoutes);
app.use('/reviews', reviewRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
