module.exports = (sequelize, type) => {
    return sequelize.define('User', {
        id: {
            type: type.UUID,
            primaryKey: true
        },
        email: {
            type: type.STRING,
            allowNull: false,
            unique: true
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
            allowNull: false,
            defaultValue: 0
        },
        hundred_multiples: {
            type: type.INTEGER,
            allowNull: false,
            defaultValue: 0
        },
        nif: {
            type: type.STRING,
            allowNull: false
        },
        total_coffees: {
            type: type.INTEGER,
            allowNull: false,
            defaultValue: 0
        }
    })
}