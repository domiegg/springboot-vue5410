import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getOrderOnlineOne = (params) => {
    return getRequest('/orderOnline/getOne', params)
}
export const getOrderOnlineList = (params) => {
    return getRequest('/orderOnline/getByPage', params)
}
export const getOrderOnlineCount = (params) => {
    return getRequest('/orderOnline/count', params)
}
export const addOrderOnline = (params) => {
    return postRequest('/orderOnline/insert', params)
}
export const editOrderOnline = (params) => {
    return postRequest('/orderOnline/update', params)
}
export const addOrEditOrderOnline = (params) => {
    return postRequest('/orderOnline/insertOrUpdate', params)
}
export const deleteOrderOnline = (params) => {
    return postRequest('/orderOnline/delByIds', params)
}
export const re = (params) => {
    return getRequest('/orderOnline/re', params)
}
export const auditOrder = (params) => {
    return postRequest('/orderOnline/audit', params)
}