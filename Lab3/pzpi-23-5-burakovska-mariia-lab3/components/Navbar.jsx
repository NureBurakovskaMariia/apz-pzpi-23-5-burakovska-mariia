import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

const Navbar = () => {
    const { user, logout } = useAuth();
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <nav style={styles.navbar}>
            <div style={styles.logoGroup}>
                <Link to="/" style={styles.logo}>Pet Shelter</Link>
            </div>

            <div style={styles.linksGroup}>
                <Link to="/" style={styles.link}>{t('nav.home')}</Link>

                {user && (
                    <Link to="/profile" style={styles.link}>{t('nav.profile')}</Link>
                )}

                {user?.role === 'VOLUNTEER' && (
                    <Link to="/tasks" style={styles.link}>{t('nav.tasks')}</Link>
                )}

                {(user?.role === 'ADMIN') && (
                    <Link to="/admin" style={styles.link}>{t('nav.admin')}</Link>
                )}

                {user?.role === 'SUPERADMIN' && (
                    <Link to="/superadmin/users" style={styles.link}>{t('nav.superadmin')}</Link>
                )}
            </div>

            <div style={styles.actionsGroup}>
                {/* Перемикач мов */}
                <div style={styles.langSwitcher}>
                    <button
                        onClick={() => changeLanguage('uk')}
                        style={{...styles.langBtn, fontWeight: i18n.language === 'uk' ? 'bold' : 'normal'}}
                    >
                        UKR
                    </button>
                    <span>|</span>
                    <button
                        onClick={() => changeLanguage('en')}
                        style={{...styles.langBtn, fontWeight: i18n.language === 'en' ? 'bold' : 'normal'}}
                    >
                        ENG
                    </button>
                </div>

                {/* Кнопка Входу/Виходу */}
                {user ? (
                    <button onClick={handleLogout} style={styles.logoutBtn}>
                        {t('nav.logout')}
                    </button>
                ) : (
                    <Link to="/login" style={styles.loginBtn}>
                        {t('login.submit_btn')}
                    </Link>
                )}
            </div>
        </nav>
    );
};

const styles = {
    navbar: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '16px 32px',
        backgroundColor: 'var(--bg-main)',
        boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
        borderBottom: '1px solid var(--bg-card)'
    },
    logoGroup: {
        flex: 1
    },
    logo: {
        fontSize: '24px',
        fontWeight: 'bold',
        color: 'var(--active)',
        textDecoration: 'none'
    },
    linksGroup: {
        display: 'flex',
        gap: '24px',
        flex: 2,
        justifyContent: 'center'
    },
    link: {
        textDecoration: 'none',
        color: 'var(--text-main)',
        fontWeight: '500',
        fontSize: '16px',
        transition: 'color 0.2s'
    },
    actionsGroup: {
        flex: 1,
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        gap: '24px'
    },
    langSwitcher: {
        display: 'flex',
        gap: '8px',
        color: 'var(--grey)'
    },
    langBtn: {
        background: 'none',
        border: 'none',
        color: 'var(--text-main)',
        cursor: 'pointer',
        fontSize: '14px',
        padding: 0
    },
    loginBtn: {
        backgroundColor: 'var(--active)',
        color: 'white',
        padding: '8px 16px',
        borderRadius: '8px',
        textDecoration: 'none',
        fontWeight: 'bold'
    },
    logoutBtn: {
        backgroundColor: 'transparent',
        color: 'var(--error)',
        border: '1px solid var(--error)',
        padding: '8px 16px',
        borderRadius: '8px',
        cursor: 'pointer',
        fontWeight: 'bold'
    }
};

export default Navbar;