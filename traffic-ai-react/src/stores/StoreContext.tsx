import React from 'react';
import { rootStore } from './RootStore';

export const StoreContext = React.createContext(rootStore);

export const useStores = () => React.useContext(StoreContext);
