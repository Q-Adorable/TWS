export const saveItemToLocalStorage = (type, stateData) => {
  let storageData = JSON.parse(localStorage.getItem(type)) || []
  const item = storageData.find(item => item.id === stateData.id)
  if (item) {
    storageData = storageData.map(item => {
      if (item.id === stateData.id) {
        return stateData
      }
      return item
    })
  } else {
    storageData.push(stateData)
  }

  localStorage.setItem(type, JSON.stringify(storageData))
}

export const removeItemFromLocalStorage = (type, id) => {
  let storageData = JSON.parse(localStorage.getItem(type)) || []
  const remainData = storageData.filter(item => item.id !== id)
  localStorage.setItem(type, JSON.stringify(remainData))
}

export const getItemFromLocalStorage = (type, id) => {
  let storageData = JSON.parse(localStorage.getItem(type)) || []
  return storageData.find(item => item.id === id)
}
