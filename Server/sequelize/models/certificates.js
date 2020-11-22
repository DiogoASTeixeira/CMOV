module.exports = (sequelize, type) => {
    return sequelize.define('Certificate', {
        id: {
            type: type.UUID,
            primaryKey: true
        },
        pem : {
            type: type.STRING,
            allowNull: false
        },
        userId: {
            type: type.UUID,
            allowNull: false
        }
    })
}