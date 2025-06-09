import axiosClient from './axiosClient';

const userApi = {
    getAll() {
        const url = '/users';
        return axiosClient.get(url);
    },

    get(id) {
        const url = `/users/${id}`;
        return axiosClient.get(url);
    },

    getCurrentOwnership(id) {
        const url = `/users/${id}/current-ownership`;
        return axiosClient.get(url);
    },

    getOwnershipHistory(id) {
        const url = `/users/${id}/ownership-history`;
        return axiosClient.get(url);
    },

    create(data) {
        const url = '/users';
        return axiosClient.post(url, data);
    },

    update(id, data) {
        const url = `/users/${id}`;
        return axiosClient.put(url, data);
    },

    delete(id) {
        const url = `/users/${id}`;
        return axiosClient.delete(url);
    }
}

export default userApi; 