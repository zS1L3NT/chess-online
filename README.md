# Chess Online

![License](https://img.shields.io/github/license/zS1L3NT/chess-online?style=for-the-badge) ![Languages](https://img.shields.io/github/languages/count/zS1L3NT/chess-online?style=for-the-badge) ![Top Language](https://img.shields.io/github/languages/top/zS1L3NT/chess-online?style=for-the-badge) ![Commit Activity](https://img.shields.io/github/commit-activity/y/zS1L3NT/chess-online?style=for-the-badge) ![Last commit](https://img.shields.io/github/last-commit/zS1L3NT/chess-online?style=for-the-badge)

Chess Online is a simple untested (bug filled?) chess engine written in Java. I built it just to learn how to build a chess engine and unintentionally learn about the concept of OOP. This is the project in which I learnt about the concept of OOP, meaning that the implementation of OOP and Java isn't exactly the best.

I have no idea how to get this project up and running again so there's no **Usage** section in this README.md

## Motivation

I found coding interesting and I wanted to tacke one of the difficult languages (Java) with a difficult project (Chess Engine) but following a tutorial first.

## Subrepositories

### [`java-chess-offline`](java-chess-offline)

This is the first implementation of the chess engine, which doesn't have the ability to let different users play against each other. It is a CLI application that asks for the move of the moving current player.

### [`java-chess-online`](java-chess-online)

This is a fork of the [`java-chess-offline`](#java-chess-offline) but with extra functionality to connect with an express server to be able to send moves from one client to another.

### [`web-chess-online`](web-chess-online)

This is a simple unorganized Express server that serves information to the [`java-chess-online`](#java-chess-online) project.

## Credits

The tutorial I followed to learn Java and a Chess Engine is [here](https://www.youtube.com/playlist?list=PLmtUDrTGI8XdWexe_FSWdZs9tC-cMEZxW)

## Built with

-   Java
    -   Network
        -   [![com.squareup.okhttp](https://img.shields.io/badge/com.squareup.okhttp-2.7.5-blue?style=flat-square)](https://mvnrepository.com/artifact/com.squareup.okhttp/okhttp/2.7.5)
        -   [![org.json](https://img.shields.io/badge/org.json-20201115-blue?style=flat-square)](https://mvnrepository.com/artifact/org.json/json/20201115)
-   Express
    -   TypeScript
        -   [![@types/express](https://img.shields.io/badge/%40types%2Fexpress-%5E4.17.9-red?style=flat-square)](https://npmjs.com/package/@types/express/v/4.17.9)
        -   [![@types/node](https://img.shields.io/badge/%40types%2Fnode-%5E14.14.12-red?style=flat-square)](https://npmjs.com/package/@types/node/v/14.14.12)
        -   [![ts-node](https://img.shields.io/badge/ts--node-%5E9.0.0-red?style=flat-square)](https://npmjs.com/package/ts-node/v/9.0.0)
        -   [![typescript](https://img.shields.io/badge/typescript-%5E4.1.2-red?style=flat-square)](https://npmjs.com/package/typescript/v/4.1.2)
    -   Miscellaneous
        -   [![express](https://img.shields.io/badge/express-%5E4.17.1-red?style=flat-square)](https://npmjs.com/package/express/v/4.17.1)
        -   [![nodemon](https://img.shields.io/badge/nodemon-%5E2.0.6-red?style=flat-square)](https://npmjs.com/package/nodemon/v/2.0.6)
        -   [![socket.io](https://img.shields.io/badge/socket.io-%5E3.0.4-red?style=flat-square)](https://npmjs.com/package/socket.io/v/3.0.4)
