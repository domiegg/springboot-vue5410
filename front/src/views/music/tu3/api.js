import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getAntvVoList = (params) => {
    return getRequest('/orderOnline/getAntvVoList3', params)
}