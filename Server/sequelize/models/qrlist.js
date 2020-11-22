module.exports = (sequelize, type) => {
    return sequelize.define('Qrlist', {
        id: {
            type: type.UUID,
            primaryKey: true
        }
    })
}