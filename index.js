import { NativeModuels } from 'react-native'

export const setAlias = (alias) => {
    NativeModuels.ANP.setAlias(alias)
}

export const unAlias = (alias) => {
    NativeModuels.ANP.unAlias(alias)
}