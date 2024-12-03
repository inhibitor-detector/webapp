import { Outlet, Navigate } from 'react-router-dom'
import { useAuth } from './Auth/AuthContext';

const PrivateRoutes = () => {
    const { token } = useAuth();

    return (
        token ? <Outlet/> : <Navigate to="/"/>
    )
}

export default PrivateRoutes
