/**
 * Created by Vaghula krishnan on 21-01-2015.
 */

'use strict';

var otpCtrl = require("../../api/otp");
var createCtrl=require("../../api/user");
var syncCtrl=require("../../api/sync");
var regCtrl=require("../../api/reg");
var sendCtrl=require("../../api/send");
var accountCtrl=require("../../api/accounts");
module.exports = {


    '/pura/otp' : {
        methods: ['post'],
        fn: [otpCtrl.generate]
    },
	'/pura/create' : {
        methods: ['get'],
        fn: [createCtrl.createuser]
    },
	'/pura/checkOtp' : {
        methods: ['post'],
        fn: [otpCtrl.checkOtp]
    },
	'/pura/sync' : {
        methods: ['post'],
        fn: [syncCtrl.sync]
    },
    '/pura/reg' : {
        methods: ['post'],
        fn: [regCtrl.reg]
    },
	'/pura/send' : {
        methods: ['post'],
        fn: [sendCtrl.send]
    },
	'/pura/accounts' : {
        methods: ['post'],
        fn: [accountCtrl.accounts]
    },
};