declare module '../api/index.js' {
    import { userApi } from './user';
    import { productApi } from './product';

    export default {
        userApi: typeof userApi,
        productApi: typeof productApi,
    };
}
