import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getOrderCommentOne = (params) => {
    return getRequest('/orderComment/getOne', params)
}
export const getOrderCommentList = (params) => {
    return getRequest('/orderComment/getByPage', params)
}
export const getOrderCommentCount = (params) => {
    return getRequest('/orderComment/count', params)
}
export const addOrderComment = (params) => {
    return postRequest('/orderComment/insert', params)
}
export const editOrderComment = (params) => {
    return postRequest('/orderComment/update', params)
}
export const addOrEditOrderComment = (params) => {
    return postRequest('/orderComment/insertOrUpdate', params)
}
export const deleteOrderComment = (params) => {
    return postRequest('/orderComment/delByIds', params)
}
export const getOrderOnlineList = (params) => {
    return getRequest('/orderOnline/getAll', params)
}