module.exports = {

  css: {
    loaderOptions: {
      scss: {
        additionalData: `@import "@/scss/main.scss";`,
      }
    },
  },
  // required for mocha https://stackoverflow.com/questions/61031121/vue-js-with-mocha-and-styles-resources-loader-cant-load-dependency-sass
  chainWebpack: config => {
    if (
      process.env.NODE_ENV === "test" ||
      process.env.NODE_ENV === "production"
    ) {
      const scssRule = config.module.rule("scss");
      scssRule.uses.clear();
      scssRule.use("null-loader").loader("null-loader");
    }
  },
  configureWebpack: {
    devtool: "source-map",
  },
};
