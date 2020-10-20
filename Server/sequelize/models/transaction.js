module.exports = (sequelize, type) => {
    return sequelize.define('Transaction', {
        id: {
            type: type.UUID,
            primaryKey: true
        },
        total_value:{
            type: type.FLOAT,
            allowNull: false
        },
        discount:{
            type: type.FLOAT,
            allowNull: false
        }
    })
}