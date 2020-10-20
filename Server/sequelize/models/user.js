module.exports = (sequelize, type) => {
    return sequelize.define('User', {
        id: {
            type: type.UUID,
            primaryKey: true
        },
        username: {
            type: type.STRING,
            allowNull: false,
            unique: true
        },
        name: {
            type: type.STRING,
            allowNull: false
        },
        password: {
            type: type.STRING,
            allowNull: false
        },
        card_number: {
            type: type.STRING,
            allowNull: false
        },
        card_cvs: {
            type: type.STRING,
            allowNull: false
        },
        total_spent: {
            type: type.FLOAT,
            allowNull: false
        },
        stored_discount: {
            type: type.FLOAT,
            allowNull: false
        },
        nif: {
            type: type.STRING,
            allowNull: false
        },
        total_coffees: {
            type: type.INTEGER,
            allowNull: false
        }
    })
}