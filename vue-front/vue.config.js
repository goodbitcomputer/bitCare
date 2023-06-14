const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true
})

// vue 설정
module.exports = {
    //런타임컴파일러 없으면 개발자창에 컴파일에러 문구 계속 뜸
    runtimeCompiler: true,

    //outputDir : npm run build로 빌드 시에 파일이 생성되는 위치
    outputDir: "../src/main/resources/static",

    //// indexPath : index.html 파일이 생성될 위치
    // indexPath: "../static/index.html",
    indexPath: "index.html",


    //// SpringBoot 서버 접속 주소
    // devServer: {
    //   // proxy: "http://localhost:8080"
    //   proxy: {x
    //     '/': {
    //       target: "http://localhost:8080/",
    //       changeOrigin: true,
    //     }
    //   }
    // },
    //
    // devServer: {
    //     proxy : 'http://localhost:8080',
    //     headers: {
    //         "Access-Control-Allow-Origin": "*",
    //         "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
    //         "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
    //     }
    // },


    chainWebpack: config => {
        const svgRule = config.module.rule("svg");
        svgRule.uses.clear();
        svgRule.use("vue-svg-loader").loader("vue-svg-loader");
    },
    css: {
        loaderOptions: {
            sass: {
                additionalData:
                    `@import "@/scss/main.scss";
                
                    `

            }
        }
    },
    // devServer: {
    //     https: true
    // },
    // configureWebpack: {
    //     module: {
    //         rules: [
    //             {
    //                 test: /\.(png|jpe?g|gif)$/i,
    //                 use: [
    //                     {
    //                         loader: 'file-loader',
    //                         options: {
    //                             name: 'assets/img/[name].[hash:8].[ext]'
    //                         }
    //                     }
    //                 ]
    //             }
    //         ]
    //     }
    // }

};