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
            requestOptions.headers.append("Authorization", `Bearer ${sessionStorage.getItem("access_token")}`);
        } else {
            const headers = new Headers();
            headers.append("Authorization", `Bearer ${sessionStorage.getItem("access_token")}`);
            requestOptions.headers = headers;
        }
    }
    try {
        const res = await fetch(url, requestOptions);
        const code = res.status;
        if (code === 204) {
            return null;
        }
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
    const tokenUrl = process.env.REACT_APP_TOKEN_URL;
    return request(`${tokenUrl}?grant_type=authorization_code&client_id=${clientId}&redirect_uri=http%3A%2F%2Flocalhost%3A3000&code=${code}`, requestOptions, true);
}

export async function getCognitoUser(accessToken) {
    const headers = new Headers();
    headers.append("Content-Type", "application/x-amz-json-1.1");
    headers.append("X-Amz-Target", "AWSCognitoIdentityProviderService.GetUser");
    headers.append("Content-Length", "1162");
    const body = { "AccessToken": accessToken }
    const requestOptions = {
        method: 'POST',
        headers,
        body: JSON.stringify(body)
    };

    return request(`https://cognito-idp.us-east-1.amazonaws.com/`, requestOptions, true);
}

export async function getUser(username) {
    const requestOptions = {
        method: 'GET',
    };

    const url = `users/${encodeURI(username)}`;

    return request(url, requestOptions);
}

export async function getMyLocations() {
    const requestOptions = {
        method: 'GET',
    };

    const url = `locations/mine`;

    return request(url, requestOptions);
}

export async function getMuseums() {
    const requestOptions = {
        method: 'GET',
    };

    const url = `locations/search?type=museum`;

    return request(url, requestOptions);
}

export async function getMuseumByName(name) {


    const requestOptions = {
        method: 'GET',
    };

    const url = `locations/${encodeURI(name)}`;

    return request(url, requestOptions);
}

export async function updateMuseum(body) {
    const name = body.name;
    delete body.name

    const requestOptions = {
        method: 'PUT',
        body: JSON.stringify(body),
        headers: getJsonHeaders()
    };

    const url = `locations/${encodeURI(name)}`;

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
        method: 'GET'
    };

    const url = `locations/${encodeURI(museum)}/ratingHistogram`;

    return request(url, requestOptions);
}

export async function getReviewsForMuseum(museumId) {

    const requestOptions = {
        method: 'GET'
    };

    const url = `reviews?locationId=${museumId}`;

    return request(url, requestOptions);
}

export async function replyToReview(reviewId, userId, comment) {
    const body = {
        review_id: reviewId,
        user_id: userId,
        reply_comment: comment
    }
    const requestOptions = {
        method: 'POST',
        body: JSON.stringify(body),
        headers: getJsonHeaders()
    };

    const url = `replies/`;

    return request(url, requestOptions);
}

export async function getRepliesToReview(reviewId) {

    const requestOptions = {
        method: 'GET'
    };

    const url = `replies/review/${reviewId}`;

    return request(url, requestOptions);
}

export async function deleteReply(replyId) {

    const requestOptions = {
        method: 'DELETE'
    };

    const url = `replies/${replyId}`;

    return request(url, requestOptions);
}