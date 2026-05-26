import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const SuperAdminPanel = () => {
    const { t } = useTranslation();
    const { user } = useAuth();
    const navigate = useNavigate();

    const [dbStats, setDbStats] = useState([]);
    const [activeTab, setActiveTab] = useState('users');
    const [usersList, setUsersList] = useState([]);
    const fileInputRef = useRef(null);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingUser, setEditingUser] = useState(null);
    const [userFormData, setUserFormData] = useState({
        name: '', email: '', role: 'USER'
    });

    useEffect(() => {
        if (!user || user.role !== 'SUPERADMIN') {
            navigate('/');
        }
    }, [user, navigate]);

    useEffect(() => {
        if (activeTab === 'users') {
            const fetchUsers = async () => {
                try {
                    const res = await axios.get('http://localhost:3000/api/admin/users');
                    setUsersList(res.data);
                } catch (err) {
                    console.error("Error fetching users:", err);
                }
            };
            fetchUsers();
        }

        if (activeTab === 'system') {
            const fetchSystemStats = async () => {
                try {
                    const res = await axios.get('http://localhost:3000/api/superadmin/system-stats');
                    setDbStats(res.data);
                } catch (err) {
                    console.error("Error fetching system stats:", err);
                }
            };
            fetchSystemStats();
        }
    }, [activeTab]);

    const handleInputChange = (e) => {
        setUserFormData({ ...userFormData, [e.target.name]: e.target.value });
    };

    const openEditModal = (u) => {
        setEditingUser(u);
        setUserFormData({ name: u.name, email: u.email, role: u.role });
        setIsModalOpen(true);
    };

    const handleSaveUser = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:3000/api/admin/users/${editingUser.id}`, userFormData);

            setUsersList(usersList.map(u => u.id === editingUser.id ? { ...u, ...userFormData } : u));
            setIsModalOpen(false);
        } catch (error) {
            console.error("Failed to update user:", error);
            alert("Помилка при оновленні користувача.");
        }
    };

    const handleDeleteUser = async (id) => {
        if (id === user.id) {
            alert("Ви не можете видалити власний акаунт!");
            return;
        }

        if (window.confirm(t('superadmin.users.delete_confirm'))) {
            try {
                await axios.delete(`http://localhost:3000/api/admin/users/${id}`);
                setUsersList(usersList.filter(u => u.id !== id));
            } catch (error) {
                console.error("Failed to delete user:", error);
                alert("Помилка при видаленні. Можливо, у користувача є пов'язані дані (завдання, заявки).");
            }
        }
    };

    const handleExport = async () => {
        try {
            const res = await axios.get('http://localhost:3000/api/superadmin/export', {
                responseType: 'blob'
            });

            const url = window.URL.createObjectURL(new Blob([res.data]));
            const link = document.createElement('a');
            link.href = url;

            const date = new Date().toISOString().split('T')[0];
            link.setAttribute('download', `petshelter_backup_${date}.json`);

            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);

        } catch (error) {
            console.error("Export failed:", error);
            alert("Помилка при експорті бази даних.");
        }
    };

    const handleImportClick = () => fileInputRef.current.click();

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        if (window.confirm("Увага! Імпорт перезапише поточні дані системи. Продовжити?")) {
            try {
                const formData = new FormData();
                formData.append('backup', file);

                await axios.post('http://localhost:3000/api/superadmin/import', formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });

                alert(`Базу даних успішно відновлено з файлу ${file.name}!`);
                window.location.reload();
            } catch (error) {
                console.error("Import failed:", error);
                alert("Помилка при імпорті бази даних. Перевірте формат файлу.");
            }
        }
        e.target.value = null;
    };

    if (!user) return null;

    return (
        <div style={styles.layout}>
            <aside style={styles.sidebar}>
                <h2 style={styles.sidebarTitle}>Super Admin</h2>
                <nav style={styles.nav}>
                    <button style={{...styles.navBtn, ...(activeTab === 'users' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('users')}>
                        👥 {t('superadmin.tabs.users')}
                    </button>
                    <button style={{...styles.navBtn, ...(activeTab === 'system' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('system')}>
                        ⚙️ {t('superadmin.tabs.system')}
                    </button>
                    <button style={{...styles.navBtn, ...(activeTab === 'backups' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('backups')}>
                        💾 {t('superadmin.tabs.backups')}
                    </button>
                </nav>
            </aside>

            <main style={styles.content}>

                {activeTab === 'users' && (
                    <div>
                        <h2 style={styles.pageTitle}>{t('superadmin.users.title')}</h2>
                        <div style={styles.tableCard}>
                            <table style={styles.table}>
                                <thead>
                                <tr>
                                    <th style={styles.th}>{t('superadmin.users.th_id')}</th>
                                    <th style={styles.th}>{t('superadmin.users.th_name')}</th>
                                    <th style={styles.th}>{t('superadmin.users.th_email')}</th>
                                    <th style={styles.th}>{t('superadmin.users.th_role')}</th>
                                    <th style={styles.th}>{t('superadmin.users.th_actions')}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {usersList.length > 0 ? usersList.map(u => (
                                    <tr key={u.id} style={styles.tr}>
                                        <td style={styles.td}>#{u.id}</td>
                                        <td style={styles.td}><strong>{u.name}</strong></td>
                                        <td style={styles.td}>{u.email}</td>
                                        <td style={styles.td}>
                                                <span style={{...styles.roleBadge, backgroundColor: u.role === 'SUPERADMIN' ? '#FEE2E2' : (u.role === 'ADMIN' ? '#FEF3C7' : '#E0E7FF'), color: u.role === 'SUPERADMIN' ? '#991B1B' : (u.role === 'ADMIN' ? '#92400E' : '#3730A3')}}>
                                                    {u.role}
                                                </span>
                                        </td>
                                        <td style={styles.td}>
                                            <div style={{ display: 'flex', gap: '12px' }}>
                                                <button style={styles.actionBtn} onClick={() => openEditModal(u)}>
                                                    {t('superadmin.users.btn_edit')}
                                                </button>
                                                <button
                                                    style={{...styles.actionBtn, color: '#EF4444', opacity: u.id === user.id ? 0.5 : 1 }}
                                                    onClick={() => handleDeleteUser(u.id)}
                                                    disabled={u.id === user.id}
                                                >
                                                    {t('superadmin.users.btn_delete')}
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                )) : <tr><td colSpan="5" style={styles.emptyTd}>{t('superadmin.users.no_data')}</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === 'backups' && (
                    <div>
                        <h2 style={styles.pageTitle}>{t('superadmin.backups.title')}</h2>
                        <div style={styles.cardsGrid}>
                            <div style={styles.card}>
                                <h3 style={styles.cardTitle}>📤 {t('superadmin.backups.export_title')}</h3>
                                <p style={styles.cardDesc}>{t('superadmin.backups.export_desc')}</p>
                                <button style={styles.primaryBtn} onClick={handleExport}>{t('superadmin.backups.btn_export')}</button>
                            </div>
                            <div style={styles.card}>
                                <h3 style={styles.cardTitle}>📥 {t('superadmin.backups.import_title')}</h3>
                                <p style={styles.cardDesc}>{t('superadmin.backups.import_desc')}</p>
                                <input type="file" accept=".json" style={{ display: 'none' }} ref={fileInputRef} onChange={handleFileChange} />
                                <button style={{...styles.primaryBtn, backgroundColor: '#EF4444'}} onClick={handleImportClick}>{t('superadmin.backups.btn_import')}</button>
                            </div>
                        </div>
                    </div>
                )}

                {activeTab === 'system' && (
                    <div>
                        <h2 style={styles.pageTitle}>{t('superadmin.system.title')}</h2>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px' }}>
                            {/* БЛОК 1: Статистика БД */}
                            <div style={styles.card}>
                                <h3 style={styles.cardTitle}>🗄️ {t('superadmin.system.db_title')}</h3>
                                <p style={styles.cardDesc}>{t('superadmin.system.db_desc')}</p>

                                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '12px' }}>
                                    <tbody>
                                    {dbStats.map((stat, idx) => (
                                        <tr key={idx} style={{ borderBottom: '1px solid #E2E8F0' }}>
                                            <td style={{ padding: '12px 0', color: '#475569', fontWeight: '500' }}>{stat.table_name}</td>
                                            <td style={{ padding: '12px 0', textAlign: 'right', fontWeight: 'bold', color: '#0F172A' }}>
                                                {stat.row_count}
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                )}
            </main>

            {isModalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modalContent}>
                        <h3 style={{marginTop: 0}}>{t('superadmin.users.modal_edit')} (#{editingUser?.id})</h3>
                        <form onSubmit={handleSaveUser} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('superadmin.users.form_name')}</label>
                                <input required type="text" name="name" value={userFormData.name} onChange={handleInputChange} style={styles.input} />
                            </div>

                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('superadmin.users.form_email')}</label>
                                <input required type="email" name="email" value={userFormData.email} onChange={handleInputChange} style={styles.input} />
                            </div>

                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('superadmin.users.form_role')}</label>
                                <select name="role" value={userFormData.role} onChange={handleInputChange} style={styles.input}>
                                    <option value="USER">USER (Звичайний користувач)</option>
                                    <option value="VOLUNTEER">VOLUNTEER (Волонтер)</option>
                                    <option value="ADMIN">ADMIN (Адміністратор)</option>
                                    <option value="SUPERADMIN">SUPERADMIN (Суперадмін)</option>
                                </select>
                            </div>

                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '16px' }}>
                                <button type="button" onClick={() => setIsModalOpen(false)} style={styles.cancelBtn}>{t('superadmin.users.btn_cancel')}</button>
                                <button type="submit" style={styles.primaryBtn}>{t('superadmin.users.btn_save')}</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

const styles = {
    layout: { display: 'flex', minHeight: 'calc(100vh - 74px)', backgroundColor: '#F8FAFC' },
    sidebar: { width: '260px', backgroundColor: '#FFFFFF', borderRight: '1px solid #E2E8F0', padding: '24px', display: 'flex', flexDirection: 'column', gap: '24px' },
    sidebarTitle: { margin: 0, fontSize: '20px', color: '#1E293B', fontWeight: 'bold' },
    nav: { display: 'flex', flexDirection: 'column', gap: '8px' },
    navBtn: { padding: '12px 16px', borderRadius: '8px', border: 'none', backgroundColor: 'transparent', color: '#64748B', fontSize: '15px', fontWeight: '600', textAlign: 'left', cursor: 'pointer', transition: 'all 0.2s', display: 'flex', gap: '12px', alignItems: 'center' },
    navBtnActive: { backgroundColor: '#EFF6FF', color: '#3B82F6' },

    content: { flex: 1, padding: '32px 48px', overflowY: 'auto' },
    pageTitle: { margin: '0 0 24px 0', fontSize: '28px', color: '#0F172A', fontWeight: 'bold' },

    tableCard: { backgroundColor: '#FFFFFF', borderRadius: '12px', border: '1px solid #E2E8F0', overflow: 'hidden' },
    table: { width: '100%', borderCollapse: 'collapse', textAlign: 'left' },
    th: { padding: '16px 24px', backgroundColor: '#F8FAFC', color: '#64748B', fontWeight: '600', fontSize: '14px', borderBottom: '1px solid #E2E8F0' },
    tr: { borderBottom: '1px solid #E2E8F0' },
    td: { padding: '16px 24px', color: '#334155', fontSize: '15px' },
    emptyTd: { padding: '32px', textAlign: 'center', color: '#94A3B8' },
    roleBadge: { padding: '4px 12px', borderRadius: '20px', fontWeight: 'bold', fontSize: '12px' },

    actionBtn: { backgroundColor: 'transparent', border: 'none', color: '#3B82F6', fontWeight: '600', cursor: 'pointer', padding: '4px 8px' },
    primaryBtn: { backgroundColor: '#3B82F6', color: 'white', padding: '10px 20px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer', transition: 'opacity 0.2s' },
    cancelBtn: { backgroundColor: '#F1F5F9', color: '#475569', padding: '10px 20px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer' },

    cardsGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '24px' },
    card: { backgroundColor: '#FFFFFF', padding: '24px', borderRadius: '12px', border: '1px solid #E2E8F0', display: 'flex', flexDirection: 'column', alignItems: 'flex-start' },
    cardTitle: { margin: '0 0 12px 0', fontSize: '18px', color: '#0F172A' },
    cardDesc: { margin: '0 0 20px 0', fontSize: '14px', color: '#64748B', lineHeight: '1.5' },

    modalOverlay: { position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(15, 23, 42, 0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000 },
    modalContent: { backgroundColor: 'white', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '400px', boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)' },
    inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px', marginBottom: '8px' },
    label: { fontSize: '14px', fontWeight: '600', color: '#475569' },
    input: { padding: '10px 12px', borderRadius: '8px', border: '1px solid #CBD5E1', fontSize: '15px', outline: 'none' }
};

export default SuperAdminPanel;