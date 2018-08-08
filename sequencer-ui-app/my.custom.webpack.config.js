var merge = require("webpack-merge")
var commonConfig = require("./common.webpack.config.js")

module.exports = merge(commonConfig, {
    entry: './path/to/my/entry/file.js'
});