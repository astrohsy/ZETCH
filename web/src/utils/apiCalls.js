const clientId = process.env.REACT_APP_COGNITO_CLIENT_ID;


const getJsonHeaders = () => {
    const headers = new Headers();
    headers.append("Content-Type", "application/json; charset=utf-8");
    headers.append("Accept", 'application/json');
    return headers;
}
const request = async (url, requestOptions, isRawUrl = false) => {
    if (!isRawUrl) {
        url = "https://zetch.tech/" + url;

        if (requestOptions.headers) {
            requestOptions.headers.append("Authorization", `Bearer ${sessionStorage.getItem("token")}`);
        } else {
            const headers = new Headers();
            headers.append("Authorization", `Bearer ${sessionStorage.getItem("token")}`);
            requestOptions.headers = headers;
        }
    }
    try {
        const res = await fetch(url, requestOptions);
        const response = await res.json()
        const returned = await JSON.parse(JSON.stringify(response));
        // console.log('Success', returned);
        return await returned;
    } catch (err) {
        console.log("Error: " + err, "input: ", { url, ...requestOptions });
    }
}

export async function getAuthToken(code) {
    const headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    const requestOptions = {
        method: 'POST',
        headers
    };

    return request(`https://zetch-app-4.auth.us-east-1.amazoncognito.com/oauth2/token?grant_type=authorization_code&client_id=${clientId}&redirect_uri=http%3A%2F%2Flocalhost%3A8080&code=${code}`, requestOptions, true);
}

export async function getMuseumByName(name) {


    const requestOptions = {
        method: 'GET',
    };

    const url = `locations/${encodeURI(name)}`;

    return request(url, requestOptions);
}

export async function updateMuseum(body) {

    const requestOptions = {
        method: 'PUT',
        body: JSON.stringify(body),
        headers: getJsonHeaders()
    };

    const url = `locations/${encodeURI(body.name)}`;

    return request(url, requestOptions);
}

export async function getAverageRatingOfMuseum(museum) {

    const requestOptions = {
        method: 'GET'
    };

    const url = `locations/${encodeURI(museum)}/averageRating`;

    return request(url, requestOptions);
}

export async function getRatingHistogramOfMuseum(museum) {

    const requestOptions = {
        method: 'DELETE'
    };

    const url = `locations/${encodeURI(museum)}/ratingHistogram`;

    return request(url, requestOptions);
}