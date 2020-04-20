export function jsonToArray(json) {
    const arr = []
    Object.keys(json).forEach(key => arr.push({name: key, value: json[key]}))
    return arr;
}


