module.exports = (sequelize, type) => {
    return sequelize.define('Voucher', {
        id: {
            type: type.UUID,
            primaryKey: true
        },
        used:{
            type: type.BOOLEAN,
            defaultValue: false,
        }
    })
}