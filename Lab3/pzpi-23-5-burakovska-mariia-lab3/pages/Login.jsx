import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; // Додали Link
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

const Login = () => {
    const { t } = useTranslation();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await axios.post('http://localhost:3000/api/login', {
                email,
                password_hash: password
            });
            login(response.data);
            navigate('/profile');
        } catch (err) {
            console.error("Login failed:", err);
            setError(t('login.error_invalid'));
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>{t('login.title')}</h2>

                {error && <p style={styles.error}>{error}</p>}

                <form onSubmit={handleSubmit} style={styles.form}>
                    <input
                        type="email"
                        placeholder={t('login.email_placeholder')}
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={styles.input}
                        required
                    />
                    <input
                        type="password"
                        placeholder={t('login.password_placeholder')}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={styles.input}
                        required
                    />
                    <button type="submit" style={styles.button}>
                        {t('login.submit_btn')}
                    </button>
                </form>

                <div style={styles.footer}>
                    <span style={styles.footerText}>{t('login.no_account')}</span>
                    <Link to="/register" style={styles.link}>{t('login.to_register')}</Link>
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: 'calc(100vh - 74px)',
        backgroundColor: 'var(--bg-main)',
        boxSizing: 'border-box',
        padding: '20px'
    },
    card: {
        backgroundColor: 'var(--bg-card)',
        padding: '40px',
        borderRadius: '16px',
        boxShadow: '0 4px 20px rgba(245, 117, 54, 0.08)',
        width: '100%',
        maxWidth: '400px'
    },
    title: { textAlign: 'center', marginBottom: '24px', color: 'var(--text-main)', fontWeight: '700' },
    form: { display: 'flex', flexDirection: 'column', gap: '16px' },
    input: { padding: '12px', borderRadius: '8px', border: '1px solid var(--grey)', outline: 'none', fontSize: '16px' },
    button: { padding: '14px', borderRadius: '8px', border: 'none', backgroundColor: 'var(--active)', color: 'white', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer', marginTop: '8px', transition: 'background-color 0.2s' },
    error: { color: 'var(--error)', textAlign: 'center', marginBottom: '16px', fontWeight: '500' },

    footer: { marginTop: '24px', textAlign: 'center', fontSize: '14px' },
    footerText: { color: 'var(--grey)', marginRight: '8px' },
    link: { color: 'var(--active)', textDecoration: 'none', fontWeight: '600' }
};

export default Login;