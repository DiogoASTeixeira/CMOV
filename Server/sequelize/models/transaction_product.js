module.exports = (sequelize, type) => {
    return sequelize.define('TransactionProduct', {
      id: {
            type: type.UUID,
            primaryKey: true
        },
        ProductId: {
            type: type.UUID,
            allowNull: false,
            references: {
              model: 'Products',
              key: 'id'
            }
          },
          TransactionId: {
            type: type.UUID,
            allowNull: false,
            references: {
              model: 'Transactions',
              key: 'id'
            }
          },
          count:{
            type: type.INTEGER
          }
    })
}