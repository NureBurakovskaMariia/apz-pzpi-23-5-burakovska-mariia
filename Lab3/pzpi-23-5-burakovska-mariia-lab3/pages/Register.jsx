import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { useTranslation } from 'react-i18next';

const Register = () => {
    const { t } = useTranslation();
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            await axios.post('http://localhost:3000/api/register', {
                name: name,
                email: email,
                password_hash: password,
                role: 'USER'
            });

            navigate('/login');
        } catch (err) {
            console.error("Registration failed:", err);
            setError(t('register.error_general'));
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>{t('register.title')}</h2>

                {error && <p style={styles.error}>{error}</p>}

                <form onSubmit={handleSubmit} style={styles.form}>
                    <input
                        type="text"
                        placeholder={t('register.name_placeholder')}
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        style={styles.input}
                        required
                    />
                    <input
                        type="email"
                        placeholder={t('register.email_placeholder')}
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={styles.input}
                        required
                    />
                    <input
                        type="password"
                        placeholder={t('register.password_placeholder')}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={styles.input}
                        required
                        minLength="6"
                    />
                    <button type="submit" style={styles.button}>
                        {t('register.submit_btn')}
                    </button>
                </form>

                <div style={styles.footer}>
                    <span style={styles.footerText}>{t('register.have_account')}</span>
                    <Link to="/login" style={styles.link}>{t('register.to_login')}</Link>
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 'calc(100vh - 74px)', backgroundColor: 'var(--bg-main)', boxSizing: 'border-box', padding: '20px' },
    card: { backgroundColor: 'var(--bg-card)', padding: '40px', borderRadius: '16px', boxShadow: '0 4px 20px rgba(245, 117, 54, 0.08)', width: '100%', maxWidth: '400px' },
    title: { textAlign: 'center', marginBottom: '24px', color: 'var(--text-main)', fontWeight: '700' },
    form: { display: 'flex', flexDirection: 'column', gap: '16px' },
    input: { padding: '12px', borderRadius: '8px', border: '1px solid var(--grey)', outline: 'none', fontSize: '16px' },
    button: { padding: '14px', borderRadius: '8px', border: 'none', backgroundColor: 'var(--active)', color: 'white', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer', marginTop: '8px', transition: 'background-color 0.2s' },
    error: { color: 'var(--error)', textAlign: 'center', marginBottom: '16px', fontWeight: '500' },
    footer: { marginTop: '24px', textAlign: 'center', fontSize: '14px' },
    footerText: { color: 'var(--grey)', marginRight: '8px' },
    link: { color: 'var(--active)', textDecoration: 'none', fontWeight: '600' }
};

export default Register;