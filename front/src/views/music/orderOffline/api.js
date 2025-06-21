import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getOrderOfflineOne = (params) => {
    return getRequest('/orderOffline/getOne', params)
}
export const getOrderOfflineList = (params) => {
    return getRequest('/orderOffline/getByPage', params)
}
export const getOrderOfflineCount = (params) => {
    return getRequest('/orderOffline/count', params)
}
export const addOrderOffline = (params) => {
    return postRequest('/orderOffline/insert', params)
}
export const editOrderOffline = (params) => {
    return postRequest('/orderOffline/update', params)
}
export const addOrEditOrderOffline = (params) => {
    return postRequest('/orderOffline/insertOrUpdate', params)
}
export const deleteOrderOffline = (params) => {
    return postRequest('/orderOffline/delByIds', params)
}
export const re = (params) => {
    return getRequest('/orderOffline/re', params)
}