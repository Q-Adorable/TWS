import HTTP_METHOD from './httpMethod'

export const get = async url => {
  try {
    const res = await fetch(url, {
      method: HTTP_METHOD.GET,
      credentials: 'include',
      headers: new Headers({
        Accept: 'application/json;charset=utf-8',
        id: 1
      })
    })

    const status = res.status

    if (res.ok) {
      const body = await res.json()
      return Object.assign({}, { body }, { status })
    }

    return { status }
  } catch (ex) {
    // alert(ex)
    return { status: ex.status }
  }
}

export const del = async url => {
  try {
    const res = await fetch(url, {
      method: HTTP_METHOD.DELETE,
      credentials: 'include',
      headers: new Headers({
        id: 1
      })
    })

    return { status: res.status }
  } catch (ex) {
    // alert(ex)
    return { status: ex.status }
  }
}

export const post = async (url, growthNote) => {
  try {
    const res = await fetch(url, {
      method: HTTP_METHOD.POST,
      credentials: 'include',
      headers: new Headers({
        'Content-Type': 'application/json;charset=utf-8',
        Accept: 'application/json',
        id: 1
      }),
      body: JSON.stringify(growthNote)
    })

    const status = res.status

    if (res.ok) {
      const body = await res.json()
      return Object.assign({}, { status }, { body })
    }

    return { status }
  } catch (ex) {
    return { status: ex.status }
  }
}

export const update = async (url, growthNote) => {
  try {
    const res = await fetch(url, {
      method: HTTP_METHOD.PUT,
      credentials: 'include',
      headers: new Headers({
        'Content-Type': 'application/json;charset=utf-8',
        Accept: 'application/json'
      }),
      body: JSON.stringify(growthNote)
    })

    return { status: res.status }
  } catch (ex) {
    return { status: ex.status }
  }
}
