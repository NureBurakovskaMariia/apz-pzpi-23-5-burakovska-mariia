import { useEffect, useState } from 'react';
import axios from 'axios';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';

const VolunteerTasks = () => {
    const { t, i18n } = useTranslation();
    const { user } = useAuth();
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!user) return;

        axios.get(`http://localhost:3000/api/users/${user.id}/tasks`)
            .then(res => {
                setTasks(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching tasks:", err);
                setError(t('tasks.error_fetch'));
                setLoading(false);
            });
    }, [user, t]);

    const formatDate = (dateString) => {
        if (!dateString) return '—';
        const date = new Date(dateString);
        return new Intl.DateTimeFormat(i18n.language, {
            year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
        }).format(date);
    };

    const handleStatusChange = async (taskId, newStatus) => {
        try {
            await axios.put(`http://localhost:3000/api/tasks/${taskId}/status`, {
                status: newStatus
            });

            setTasks(tasks.map(task =>
                task.id === taskId ? { ...task, status: newStatus } : task
            ));
        } catch (err) {
            console.error("Failed to update status:", err);
            alert(t('tasks.error_update'));
        }
    };

    const getStatusStyle = (status) => {
        // Додано обробку статусу 'open'
        switch((status || 'open').toLowerCase()) {
            case 'open':
            case 'pending': return { backgroundColor: 'rgba(245, 158, 11, 0.1)', color: 'var(--rating)' };
            case 'in_progress': return { backgroundColor: 'rgba(245, 117, 54, 0.1)', color: 'var(--active)' };
            case 'completed': return { backgroundColor: 'rgba(148, 163, 184, 0.1)', color: 'var(--grey)' };
            default: return { backgroundColor: 'rgba(148, 163, 184, 0.1)', color: 'var(--text-main)' };
        }
    };

    if (loading) return <div style={styles.centerText}>Loading...</div>;

    return (
        <div style={styles.container}>
            <h2 style={styles.pageTitle}>{t('tasks.title')}</h2>

            {error && <p style={styles.error}>{error}</p>}

            {tasks.length === 0 && !error ? (
                <p style={styles.centerText}>{t('tasks.no_tasks')}</p>
            ) : (
                <div style={styles.grid}>
                    {tasks.map(task => (
                        <div key={task.id} style={{
                            ...styles.card,
                            opacity: task.status === 'completed' ? 0.6 : 1
                        }}>
                            <div style={styles.cardHeader}>
                                <h3 style={styles.taskTitle}>Завдання #{task.id}</h3>

                                <span style={{...styles.statusBadge, ...getStatusStyle(task.status)}}>
                                    {t(`tasks.status_${(task.status || 'open').toLowerCase()}`)}
                                </span>
                            </div>

                            <p style={styles.description}>{task.description}</p>

                            <div style={styles.infoRow}>
                                <span style={styles.label}>{t('tasks.due_date')}:</span>
                                <span style={styles.value}>{formatDate(task.due_date)}</span>
                            </div>

                            <div style={styles.actions}>
                                {(task.status === 'pending' || task.status === 'open' || !task.status) && (
                                    <button
                                        style={styles.startButton}
                                        onClick={() => handleStatusChange(task.id, 'in_progress')}
                                    >
                                        {t('tasks.btn_start')}
                                    </button>
                                )}
                                {task.status === 'in_progress' && (
                                    <button
                                        style={styles.completeButton}
                                        onClick={() => handleStatusChange(task.id, 'completed')}
                                    >
                                        {t('tasks.btn_complete')}
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

const styles = {
    container: { padding: '40px 32px', backgroundColor: 'var(--bg-main)', minHeight: 'calc(100vh - 74px)', boxSizing: 'border-box' },
    pageTitle: { margin: '0 0 32px 0', color: 'var(--text-main)', fontSize: '28px', fontWeight: '700' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '24px' },

    card: { backgroundColor: 'var(--bg-card)', borderRadius: '16px', padding: '24px', border: '1px solid #F1ECE9', boxShadow: '0 4px 12px rgba(0,0,0,0.02)', display: 'flex', flexDirection: 'column', transition: 'opacity 0.3s' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px', gap: '12px' },
    taskTitle: { margin: 0, fontSize: '20px', color: 'var(--text-main)', fontWeight: '700', lineHeight: '1.3' },
    statusBadge: { padding: '6px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: '700', whiteSpace: 'nowrap' },

    description: { fontSize: '15px', color: 'var(--text-main)', lineHeight: '1.6', marginBottom: '24px', flexGrow: 1 },

    infoRow: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '16px 0', borderTop: '1px dashed #E2E8F0', borderBottom: '1px dashed #E2E8F0', marginBottom: '20px' },
    label: { color: 'var(--grey)', fontSize: '14px', fontWeight: '500' },
    value: { color: 'var(--text-main)', fontSize: '14px', fontWeight: '600' },

    actions: { display: 'flex', gap: '12px' },
    startButton: { flex: 1, backgroundColor: 'transparent', color: 'var(--active)', border: '2px solid var(--active)', padding: '12px', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer', transition: 'all 0.2s' },
    completeButton: { flex: 1, backgroundColor: 'var(--active)', color: 'white', border: 'none', padding: '12px', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer', transition: 'background-color 0.2s' },

    centerText: { textAlign: 'center', padding: '40px', color: 'var(--grey)', fontSize: '18px' },
    error: { color: 'var(--error)', textAlign: 'center', marginBottom: '24px', fontWeight: '500' }
};

export default VolunteerTasks;