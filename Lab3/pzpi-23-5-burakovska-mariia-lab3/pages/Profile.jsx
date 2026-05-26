import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

const Profile = () => {
    const { t } = useTranslation();
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const [donated, setDonated] = useState(false);
    const [donateType, setDonateType] = useState('money');
    const [amount, setAmount] = useState('');
    const [note, setNote] = useState('');

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleDonateSubmit = (e) => {
        e.preventDefault();
        console.log("Donation submitted:", { type: donateType, amount, note });
        setDonated(true);
    };

    const resetDonationForm = () => {
        setAmount('');
        setNote('');
        setDonated(false);
    };

    if (!user) return null;

    return (
        <div style={styles.container}>
            <h2 style={styles.pageTitle}>{t('profile.title')}</h2>

            <div style={styles.content}>
                <div style={styles.card}>
                    <h3 style={styles.cardTitle}>{t('profile.personal_info')}</h3>

                    <div style={styles.infoRow}>
                        <span style={styles.label}>{t('profile.name')}:</span>
                        <span style={styles.value}>{user.name || '—'}</span>
                    </div>

                    <div style={styles.infoRow}>
                        <span style={styles.label}>{t('profile.email')}:</span>
                        <span style={styles.value}>{user.email}</span>
                    </div>

                    <div style={styles.infoRow}>
                        <span style={styles.label}>{t('profile.role')}:</span>
                        <span style={styles.roleBadge}>
                            {t(`roles.${user.role}`)}
                        </span>
                    </div>

                    <button onClick={handleLogout} style={styles.logoutButton}>
                        {t('profile.logout_btn')}
                    </button>
                </div>

                <div style={{...styles.card, ...styles.donateCard}}>
                    <h3 style={styles.donateTitle}>{t('profile.donate_title')}</h3>

                    {donated ? (
                        <div style={styles.thanksBox}>
                            <p style={styles.thanksText}>{t('profile.thanks_message')}</p>
                            <button onClick={resetDonationForm} style={styles.donateAgainButton}>
                                {t('profile.donate_again_btn')}
                            </button>
                        </div>
                    ) : (
                        <form onSubmit={handleDonateSubmit} style={styles.form}>
                            <div style={styles.formGroup}>
                                <label style={styles.formLabel}>{t('profile.donate_type')}</label>
                                <select
                                    value={donateType}
                                    onChange={(e) => setDonateType(e.target.value)}
                                    style={styles.input}
                                >
                                    <option value="money">{t('profile.donate_type_money')}</option>
                                    <option value="food">{t('profile.donate_type_food')}</option>
                                    <option value="medicine">{t('profile.donate_type_medicine')}</option>
                                </select>
                            </div>

                            <div style={styles.formGroup}>
                                <label style={styles.formLabel}>{t('profile.donate_amount')}</label>
                                <input
                                    type="number"
                                    min="1"
                                    placeholder={donateType === 'money' ? '100' : '1'}
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    style={styles.input}
                                    required
                                />
                            </div>

                            <div style={styles.formGroup}>
                                <label style={styles.formLabel}>{t('profile.donate_note')}</label>
                                <textarea
                                    placeholder={t('profile.donate_note_placeholder')}
                                    value={note}
                                    onChange={(e) => setNote(e.target.value)}
                                    style={styles.textarea}
                                />
                            </div>

                            <button type="submit" style={styles.donateButton}>
                                {t('profile.donate_btn')}
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: { padding: '40px 32px', backgroundColor: 'var(--bg-main)', minHeight: 'calc(100vh - 74px)', boxSizing: 'border-box' },
    pageTitle: { margin: '0 0 32px 0', color: 'var(--text-main)', fontSize: '28px', fontWeight: '700' },
    content: { display: 'flex', gap: '24px', flexWrap: 'wrap', alignItems: 'flex-start' },

    card: { backgroundColor: 'var(--bg-card)', borderRadius: '16px', padding: '32px', border: '1px solid #F1ECE9', boxShadow: '0 4px 12px rgba(0,0,0,0.02)', flex: '1 1 350px', display: 'flex', flexDirection: 'column' },
    cardTitle: { margin: '0 0 24px 0', fontSize: '20px', color: 'var(--text-main)' },

    infoRow: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px 0', borderBottom: '1px solid #F1ECE9' },
    label: { color: 'var(--grey)', fontSize: '15px', fontWeight: '500' },
    value: { color: 'var(--text-main)', fontSize: '16px', fontWeight: '600' },
    roleBadge: { backgroundColor: 'rgba(245, 117, 54, 0.1)', color: 'var(--active)', padding: '6px 12px', borderRadius: '8px', fontSize: '14px', fontWeight: 'bold' },

    logoutButton: { marginTop: '24px', alignSelf: 'flex-start', backgroundColor: 'transparent', color: 'var(--error)', border: '1px solid var(--error)', padding: '10px 20px', borderRadius: '8px', fontWeight: 'bold', fontSize: '14px', cursor: 'pointer' },

    donateCard: { border: '2px solid var(--active)' },
    donateTitle: { margin: '0 0 20px 0', fontSize: '22px', color: 'var(--active)', fontWeight: '700' },

    form: { display: 'flex', flexDirection: 'column', gap: '16px' },
    formGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
    formLabel: { fontSize: '14px', color: 'var(--grey)', fontWeight: '600' },
    input: { padding: '12px', borderRadius: '8px', border: '1px solid var(--grey)', outline: 'none', fontSize: '15px', fontFamily: 'inherit', backgroundColor: 'var(--bg-main)' },
    textarea: { padding: '12px', borderRadius: '8px', border: '1px solid var(--grey)', outline: 'none', fontSize: '15px', fontFamily: 'inherit', backgroundColor: 'var(--bg-main)', minHeight: '80px', resize: 'vertical' },
    donateButton: { backgroundColor: 'var(--active)', color: 'white', border: 'none', padding: '14px', borderRadius: '8px', fontWeight: 'bold', fontSize: '16px', cursor: 'pointer', marginTop: '8px' },

    thanksBox: { display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center', height: '100%', minHeight: '200px', gap: '20px' },
    thanksText: { fontSize: '18px', color: 'var(--text-main)', fontWeight: '600', lineHeight: '1.5', margin: 0 },
    donateAgainButton: { backgroundColor: 'transparent', color: 'var(--active)', border: '2px solid var(--active)', padding: '10px 20px', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }
};

export default Profile;