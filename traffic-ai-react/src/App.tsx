import React, { useEffect } from 'react';
import { observer } from 'mobx-react-lite';
import { useStores } from './stores/StoreContext';
import IntersectionPage from './components/IntersectionPage'; 

const App: React.FC = observer(() => {
  const { configStore } = useStores();

  useEffect(() => {
    if (!configStore.isInitialized) {
      configStore.loadConfig();
    }
  }, [configStore]);

  if (configStore.isLoading || !configStore.config) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        Hello world!
      </div>
    );
  }

  return (
    <IntersectionPage />
  );
});

export default App;
