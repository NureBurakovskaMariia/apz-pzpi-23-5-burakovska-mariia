import { useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Animals from './pages/Animals';
import Register from './pages/Register';
import Profile from './pages/Profile';
import VolunteerTasks from './pages/VolunteerTasks';
import AdminPanel from './pages/AdminPanel';
import SuperAdminPanel from './pages/SuperAdminPanel';

function App() {
    const { i18n } = useTranslation();

    useEffect(() => {
        document.dir = i18n.dir();
        document.documentElement.lang = i18n.language;
    }, [i18n.language]);

    return (
        <BrowserRouter>
            <Navbar />

            <Routes>
                <Route path="/" element={<Animals />} />

                <Route path="/login" element={<Login />} />

                <Route path="/register" element={<Register />} />

                <Route element={<ProtectedRoute allowedRoles={['USER', 'VOLUNTEER', 'ADMIN', 'SUPERADMIN']} />}>
                    <Route path="/profile" element={<Profile />} />
                </Route>

                <Route element={<ProtectedRoute allowedRoles={['VOLUNTEER']} />}>
                    <Route path="/tasks" element={<VolunteerTasks />} />
                </Route>

                <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                    <Route path="/admin" element={<AdminPanel />} />
                </Route>

                <Route element={<ProtectedRoute allowedRoles={['SUPERADMIN']} />}>
                    <Route path="/superadmin/users" element={<SuperAdminPanel />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;