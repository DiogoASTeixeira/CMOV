module.exports = (sequelize, type) => {
    return sequelize.define('Certificate', {
        id: {
            type: type.INTEGER,
            primaryKey: true
        },
        subject: {
            type: type.STRING,
            allowNull: false
        },
        issuer:  {
            type: type.STRING,
            allowNull: false
        },
        version: {
            type: type.STRING,
            allowNull: false
        },
        sdate: {
            type: type.STRING,
            allowNull: false
        },
        edate: {
            type: type.STRING,
            allowNull: false
        },
        thumb: {
            type: type.STRING,
            allowNull: false
        },
        serial: {
            type: type.STRING,
            allowNull: false
        },
        friendly: {
            type: type.STRING,
            allowNull: false
        },
        pkencoding: {
            type: type.STRING,
            allowNull: false
        },
        userId: {
            type: type.UUID,
            allowNull: false
        }
    })
}