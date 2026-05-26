import { useState, useEffect } from 'react';
import axios from 'axios';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const AdminPanel = () => {
    const { t, i18n } = useTranslation();
    const { user } = useAuth();
    const navigate = useNavigate();

    const [activeTab, setActiveTab] = useState('dashboard');
    const [loading, setLoading] = useState(true);
    const todayDate = new Date().toISOString().split('T')[0];

    const [stats, setStats] = useState({ animals: null, donations: null, volunteers: [] });
    const [animals, setAnimals] = useState([]);
    const [adoptions, setAdoptions] = useState([]);
    const [tasksList, setTasksList] = useState([]);

    const [isAnimalModalOpen, setIsAnimalModalOpen] = useState(false);
    const [editingAnimal, setEditingAnimal] = useState(null);
    const [animalFormData, setAnimalFormData] = useState({
        name: '', species: '', breed: '', gender: 'Male', birth_date: '', description: '', status: 'available'
    });

    const [isTaskModalOpen, setIsTaskModalOpen] = useState(false);
    const [editingTask, setEditingTask] = useState(null);
    const [taskFormData, setTaskFormData] = useState({
        volunteer_id: '', description: '', due_date: ''
    });

    useEffect(() => {
        if (!user || user.role !== 'ADMIN') {
            navigate('/');
        }
    }, [user, navigate]);

    useEffect(() => {
        if (activeTab === 'dashboard') {
            const fetchStats = async () => {
                setLoading(true);
                try {
                    const [animalsRes, donationsRes, volunteersRes] = await Promise.all([
                        axios.get('http://localhost:3000/api/admin/animals/statistics').catch(() => ({ data: {} })),
                        axios.get('http://localhost:3000/api/admin/donations/statistics/advanced').catch(() => ({ data: {} })),
                        axios.get('http://localhost:3000/api/admin/volunteers/activity-index').catch(() => ({ data: [] }))
                    ]);
                    setStats({ animals: animalsRes.data, donations: donationsRes.data, volunteers: volunteersRes.data });
                } finally {
                    setLoading(false);
                }
            };
            fetchStats();
        }

        if (activeTab === 'animals') {
            const fetchAnimals = async () => {
                try {
                    const res = await axios.get('http://localhost:3000/api/animals');
                    setAnimals(res.data);
                } catch (err) {
                    console.error("Error fetching animals:", err);
                }
            };
            fetchAnimals();
        }

        if (activeTab === 'adoptions') {
            const fetchAdoptions = async () => {
                try {
                    const res = await axios.get('http://localhost:3000/api/adoptions');
                    setAdoptions(res.data);
                } catch (err) {
                    console.error("Error fetching adoptions:", err);
                }
            };
            fetchAdoptions();
        }

        if (activeTab === 'tasks') {
            const fetchTasks = async () => {
                try {
                    const res = await axios.get('http://localhost:3000/api/tasks');
                    setTasksList(res.data);
                } catch (err) {
                    console.error("Error fetching tasks:", err);
                }
            };
            fetchTasks();
        }
    }, [activeTab]);


    const handleAnimalInput = (e) => setAnimalFormData({ ...animalFormData, [e.target.name]: e.target.value });

    const openAddAnimalModal = () => {
        setAnimalFormData({ name: '', species: '', breed: '', gender: 'Male', birth_date: '', description: '', status: 'available' });
        setEditingAnimal(null);
        setIsAnimalModalOpen(true);
    };

    const openEditAnimalModal = (animal) => {
        const formattedDate = animal.birth_date ? animal.birth_date.split('T')[0] : '';
        setAnimalFormData({ ...animal, birth_date: formattedDate });
        setEditingAnimal(animal);
        setIsAnimalModalOpen(true);
    };

    const handleSaveAnimal = async (e) => {
        e.preventDefault();
        try {
            if (editingAnimal) {
                const res = await axios.put(`http://localhost:3000/api/animals/${editingAnimal.id}`, animalFormData);
                setAnimals(animals.map(a => a.id === editingAnimal.id ? res.data : a));
            } else {
                const res = await axios.post('http://localhost:3000/api/animals', animalFormData);
                setAnimals([...animals, res.data]);
            }
            setIsAnimalModalOpen(false);
        } catch (error) {
            console.error("Failed to save animal:", error);
            alert("Помилка при збереженні.");
        }
    };

    const handleDeleteAnimal = async (id) => {
        if (window.confirm(t('admin.animals.delete_confirm'))) {
            try {
                await axios.delete(`http://localhost:3000/api/animals/${id}`);
                setAnimals(animals.filter(a => a.id !== id));
            } catch (error) {
                console.error("Failed to delete animal:", error);
                alert("Помилка при видаленні.");
            }
        }
    };

    const handleAdoptionStatus = async (id, newStatus) => {
        try {
            const res = await axios.put(`http://localhost:3000/api/adoptions/${id}/status`, { status: newStatus });
            setAdoptions(adoptions.map(adopt => adopt.id === id ? { ...adopt, status: res.data.status || newStatus } : adopt));
        } catch (error) {
            console.error("Failed to update adoption:", error);
            alert("Помилка при оновленні статусу.");
        }
    };

    const handleTaskInput = (e) => setTaskFormData({ ...taskFormData, [e.target.name]: e.target.value });

    const openAddTaskModal = () => {
        setTaskFormData({ volunteer_id: '', description: '', due_date: '' });
        setEditingTask(null);
        setIsTaskModalOpen(true);
    };

    const openEditTaskModal = (task) => {
        const formattedDate = task.due_date ? task.due_date.split('T')[0] : '';
        setTaskFormData({ ...task, due_date: formattedDate });
        setEditingTask(task);
        setIsTaskModalOpen(true);
    };

    const handleSaveTask = async (e) => {
        e.preventDefault();
        try {
            if (editingTask) {
                const res = await axios.put(`http://localhost:3000/api/tasks/${editingTask.id}`, taskFormData);
                setTasksList(tasksList.map(t => t.id === editingTask.id ? res.data : t));
            } else {
                const dataToSend = { ...taskFormData, volunteer_id: parseInt(taskFormData.volunteer_id, 10) };
                const res = await axios.post('http://localhost:3000/api/tasks', dataToSend);
                setTasksList([...tasksList, res.data]);
            }
            setIsTaskModalOpen(false);
        } catch (error) {
            console.error("Failed to save task:", error);
            alert("Помилка при збереженні завдання.");
        }
    };

    const handleDeleteTask = async (id) => {
        if (window.confirm("Видалити це завдання?")) {
            try {
                await axios.delete(`http://localhost:3000/api/tasks/${id}`);
                setTasksList(tasksList.filter(t => t.id !== id));
            } catch (error) {
                console.error("Failed to delete task:", error);
            }
        }
    };

    const formatDate = (dateStr) => {
        if (!dateStr) return '—';
        return new Intl.DateTimeFormat(i18n.language).format(new Date(dateStr));
    };

    if (!user) return null;

    return (
        <div style={styles.layout}>
            <aside style={styles.sidebar}>
                <h2 style={styles.sidebarTitle}>{t('nav.admin')}</h2>
                <nav style={styles.nav}>
                    <button style={{...styles.navBtn, ...(activeTab === 'dashboard' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('dashboard')}>📊 {t('admin.tabs.dashboard')}</button>
                    <button style={{...styles.navBtn, ...(activeTab === 'animals' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('animals')}>🐾 {t('admin.tabs.animals')}</button>
                    <button style={{...styles.navBtn, ...(activeTab === 'adoptions' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('adoptions')}>📝 {t('admin.tabs.adoptions')}</button>
                    <button style={{...styles.navBtn, ...(activeTab === 'tasks' ? styles.navBtnActive : {})}} onClick={() => setActiveTab('tasks')}>📋 {t('admin.tabs.tasks')}</button>
                </nav>
            </aside>

            <main style={styles.content}>

                {activeTab === 'dashboard' && (
                    loading ? <p style={styles.loadingText}>{t('admin.dashboard.loading')}</p> :
                        <div>
                            <h2 style={styles.pageTitle}>{t('admin.dashboard.title')}</h2>
                            <div style={styles.statsGrid}>
                                <div style={styles.statCard}>
                                    <h3 style={styles.statTitle}>{t('admin.dashboard.animals_title')}</h3>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.animals_available')}</span> <strong>{stats.animals?.available || 0}</strong></div>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.animals_adopted')}</span> <strong>{stats.animals?.adopted || 0}</strong></div>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.animals_pending')}</span> <strong>{stats.animals?.pending || 0}</strong></div>
                                </div>
                                <div style={styles.statCard}>
                                    <h3 style={styles.statTitle}>{t('admin.dashboard.donations_title')}</h3>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.donations_total')}</span> <strong>{stats.donations?.total || 0} ₴</strong></div>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.donations_average')}</span> <strong>{Math.round(stats.donations?.average || 0)} ₴</strong></div>
                                    <div style={styles.statRow}><span>{t('admin.dashboard.donations_count')}</span> <strong>{stats.donations?.count || 0}</strong></div>
                                </div>
                            </div>
                            <h3 style={{...styles.statTitle, marginTop: '32px'}}>{t('admin.dashboard.volunteers_title')}</h3>
                            <div style={styles.tableCard}>
                                <table style={styles.table}>
                                    <thead>
                                    <tr>
                                        <th style={styles.th}>{t('admin.dashboard.th_id')}</th>
                                        <th style={styles.th}>{t('admin.dashboard.th_completed')}</th>
                                        <th style={styles.th}>{t('admin.dashboard.th_overdue')}</th>
                                        <th style={styles.th}>{t('admin.dashboard.th_index')}</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {stats.volunteers?.length > 0 ? stats.volunteers.map((vol, idx) => (
                                        <tr key={idx} style={styles.tr}>
                                            <td style={styles.td}>#{vol.volunteer_id}</td>
                                            <td style={styles.td}>{vol.completed_tasks}</td>
                                            <td style={styles.td}>{vol.overdue_tasks}</td>
                                            <td style={styles.td}><span style={styles.indexBadge}>{vol.activity_index}</span></td>
                                        </tr>
                                    )) : <tr><td colSpan="4" style={styles.emptyTd}>{t('admin.dashboard.no_volunteers')}</td></tr>}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                )}

                {activeTab === 'animals' && (
                    <div>
                        <div style={styles.tabHeader}>
                            <h2 style={styles.pageTitle}>{t('admin.animals.title')}</h2>
                            <button style={styles.primaryBtn} onClick={openAddAnimalModal}>{t('admin.animals.add_btn')}</button>
                        </div>
                        <div style={styles.tableCard}>
                            <table style={styles.table}>
                                <thead>
                                <tr>
                                    <th style={styles.th}>ID</th>
                                    <th style={styles.th}>{t('admin.animals.th_name')}</th>
                                    <th style={styles.th}>{t('admin.animals.th_species')}</th>
                                    <th style={styles.th}>{t('admin.animals.th_breed')}</th>
                                    <th style={styles.th}>{t('admin.animals.th_status')}</th>
                                    <th style={styles.th}>{t('admin.animals.th_actions')}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {animals.length > 0 ? [...animals].sort((a, b) => a.name.localeCompare(b.name, i18n.language)).map(animal => (
                                    <tr key={animal.id} style={styles.tr}>
                                        <td style={styles.td}>#{animal.id}</td>
                                        <td style={styles.td}><strong>{animal.name}</strong></td>
                                        <td style={styles.td}>{animal.species}</td>
                                        <td style={styles.td}>{animal.breed || '—'}</td>
                                        <td style={styles.td}>
                                                <span style={{...styles.indexBadge, backgroundColor: animal.status === 'available' ? '#DEF7EC' : (animal.status === 'pending' ? '#FEF3C7' : '#E2E8F0'), color: animal.status === 'available' ? '#03543F' : (animal.status === 'pending' ? '#92400E' : '#475569')}}>
                                                    {animal.status}
                                                </span>
                                        </td>
                                        <td style={styles.td}>
                                            <div style={{ display: 'flex', gap: '12px' }}>
                                                <button style={styles.actionBtn} onClick={() => openEditAnimalModal(animal)}>{t('admin.animals.btn_edit')}</button>
                                                <button style={{...styles.actionBtn, color: '#EF4444'}} onClick={() => handleDeleteAnimal(animal.id)}>{t('admin.animals.btn_delete')}</button>
                                            </div>
                                        </td>
                                    </tr>
                                )) : <tr><td colSpan="6" style={styles.emptyTd}>{t('admin.animals.no_data')}</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === 'adoptions' && (
                    <div>
                        <h2 style={styles.pageTitle}>{t('admin.adoptions.title')}</h2>
                        <div style={styles.tableCard}>
                            <table style={styles.table}>
                                <thead>
                                <tr>
                                    <th style={styles.th}>{t('admin.adoptions.th_id')}</th>
                                    <th style={styles.th}>{t('admin.adoptions.th_animal')}</th>
                                    <th style={styles.th}>{t('admin.adoptions.th_user')}</th>
                                    <th style={styles.th}>{t('admin.adoptions.th_note')}</th>
                                    <th style={styles.th}>{t('admin.adoptions.th_status')}</th>
                                    <th style={styles.th}>{t('admin.adoptions.th_actions')}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {adoptions.length > 0 ? adoptions.map(adopt => (
                                    <tr key={adopt.id} style={styles.tr}>
                                        <td style={styles.td}>#{adopt.id}</td>
                                        <td style={styles.td}>#{adopt.animal_id}</td>
                                        <td style={styles.td}>#{adopt.user_id}</td>
                                        <td style={styles.td}>{adopt.note || '—'}</td>
                                        <td style={styles.td}>
                                                <span style={{...styles.indexBadge, backgroundColor: adopt.status === 'approved' ? '#DEF7EC' : (adopt.status === 'rejected' ? '#FDE8E8' : '#FEF3C7'), color: adopt.status === 'approved' ? '#03543F' : (adopt.status === 'rejected' ? '#9B1C1C' : '#92400E')}}>
                                                    {t(`admin.adoptions.status_${(adopt.status || 'pending').toLowerCase()}`)}
                                                </span>
                                        </td>
                                        <td style={styles.td}>
                                            {adopt.status === 'pending' || !adopt.status ? (
                                                <div style={{ display: 'flex', gap: '8px' }}>
                                                    <button style={{...styles.actionBtn, color: '#10B981'}} onClick={() => handleAdoptionStatus(adopt.id, 'approved')}>{t('admin.adoptions.btn_approve')}</button>
                                                    <button style={{...styles.actionBtn, color: '#EF4444'}} onClick={() => handleAdoptionStatus(adopt.id, 'rejected')}>{t('admin.adoptions.btn_reject')}</button>
                                                </div>
                                            ) : <span style={{ color: '#94A3B8', fontSize: '14px' }}>—</span>}
                                        </td>
                                    </tr>
                                )) : <tr><td colSpan="6" style={styles.emptyTd}>{t('admin.adoptions.no_data')}</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === 'tasks' && (
                    <div>
                        <div style={styles.tabHeader}>
                            <h2 style={styles.pageTitle}>{t('admin.tasks.title')}</h2>
                            <button style={styles.primaryBtn} onClick={openAddTaskModal}>{t('admin.tasks.add_btn')}</button>
                        </div>
                        <div style={styles.tableCard}>
                            <table style={styles.table}>
                                <thead>
                                <tr>
                                    <th style={styles.th}>{t('admin.tasks.th_id')}</th>
                                    <th style={styles.th}>{t('admin.tasks.th_volunteer')}</th>
                                    <th style={styles.th}>{t('admin.tasks.th_desc')}</th>
                                    <th style={styles.th}>{t('admin.tasks.th_due')}</th>
                                    <th style={styles.th}>{t('admin.tasks.th_status')}</th>
                                    <th style={styles.th}>{t('admin.tasks.th_actions')}</th>
                                </tr>
                                </thead>
                                <tbody>
                                {tasksList.length > 0 ? tasksList.map(task => (
                                    <tr key={task.id} style={styles.tr}>
                                        <td style={styles.td}>#{task.id}</td>
                                        <td style={styles.td}><strong>#{task.volunteer_id}</strong></td>
                                        <td style={styles.td}>{task.description}</td>
                                        <td style={styles.td}>{formatDate(task.due_date)}</td>
                                        <td style={styles.td}>
                                                <span style={{...styles.indexBadge, backgroundColor: task.status === 'completed' ? '#E2E8F0' : (task.status === 'in_progress' ? '#DBEAFE' : '#FEF3C7'), color: task.status === 'completed' ? '#475569' : (task.status === 'in_progress' ? '#1E40AF' : '#92400E')}}>
                                                    {task.status || 'pending'}
                                                </span>
                                        </td>
                                        <td style={styles.td}>
                                            <div style={{ display: 'flex', gap: '12px' }}>
                                                <button style={styles.actionBtn} onClick={() => openEditTaskModal(task)}>{t('admin.animals.btn_edit')}</button>
                                                <button style={{...styles.actionBtn, color: '#EF4444'}} onClick={() => handleDeleteTask(task.id)}>{t('admin.animals.btn_delete')}</button>
                                            </div>
                                        </td>
                                    </tr>
                                )) : <tr><td colSpan="6" style={styles.emptyTd}>{t('admin.tasks.no_data')}</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </main>

            {isAnimalModalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modalContent}>
                        <h3 style={{marginTop: 0}}>{editingAnimal ? t('admin.animals.modal_edit') : t('admin.animals.modal_add')}</h3>
                        <form onSubmit={handleSaveAnimal} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('admin.animals.form_name')}</label>
                                <input required type="text" name="name" value={animalFormData.name} onChange={handleAnimalInput} style={styles.input} />
                            </div>
                            <div style={{ display: 'flex', gap: '16px' }}>
                                <div style={{...styles.inputGroup, flex: 1}}>
                                    <label style={styles.label}>{t('admin.animals.form_species')}</label>
                                    <input required type="text" name="species" value={animalFormData.species} onChange={handleAnimalInput} style={styles.input} />
                                </div>
                                <div style={{...styles.inputGroup, flex: 1}}>
                                    <label style={styles.label}>{t('admin.animals.form_breed')}</label>
                                    <input type="text" name="breed" value={animalFormData.breed} onChange={handleAnimalInput} style={styles.input} />
                                </div>
                            </div>
                            <div style={{ display: 'flex', gap: '16px' }}>
                                <div style={{...styles.inputGroup, flex: 1}}>
                                    <label style={styles.label}>{t('admin.animals.form_gender')}</label>
                                    <select name="gender" value={animalFormData.gender} onChange={handleAnimalInput} style={styles.input}>
                                        <option value="Male">{t('admin.animals.gender_male')}</option>
                                        <option value="Female">{t('admin.animals.gender_female')}</option>
                                        <option value="Unknown">{t('admin.animals.gender_unknown')}</option>
                                    </select>
                                </div>
                                <div style={{...styles.inputGroup, flex: 1}}>
                                    <label style={styles.label}>{t('admin.animals.form_date')}</label>
                                    <input type="date" name="birth_date" value={animalFormData.birth_date} onChange={handleAnimalInput} max={todayDate} style={styles.input} />
                                </div>
                            </div>
                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('admin.animals.form_status')}</label>
                                <select name="status" value={animalFormData.status} onChange={handleAnimalInput} style={styles.input}>
                                    <option value="available">{t('admin.animals.status_available')}</option>
                                    <option value="pending">{t('admin.animals.status_pending')}</option>
                                    <option value="adopted">{t('admin.animals.status_adopted')}</option>
                                </select>
                            </div>
                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('admin.animals.form_desc')}</label>
                                <textarea rows="3" name="description" value={animalFormData.description} onChange={handleAnimalInput} style={styles.input} />
                            </div>
                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '16px' }}>
                                <button type="button" onClick={() => setIsAnimalModalOpen(false)} style={styles.cancelBtn}>{t('admin.animals.btn_cancel')}</button>
                                <button type="submit" style={styles.primaryBtn}>{t('admin.animals.btn_save')}</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isTaskModalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modalContent}>
                        <h3 style={{marginTop: 0}}>{editingTask ? t('admin.tasks.modal_edit') : t('admin.tasks.modal_add')}</h3>
                        <form onSubmit={handleSaveTask} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>

                            {!editingTask && (
                                <div style={styles.inputGroup}>
                                    <label style={styles.label}>{t('admin.tasks.form_volunteer')}</label>
                                    <input required type="number" name="volunteer_id" value={taskFormData.volunteer_id} onChange={handleTaskInput} placeholder="Наприклад: 1" style={styles.input} />
                                </div>
                            )}

                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('admin.tasks.form_desc')}</label>
                                <textarea required rows="3" name="description" value={taskFormData.description} onChange={handleTaskInput} style={styles.input} />
                            </div>

                            <div style={styles.inputGroup}>
                                <label style={styles.label}>{t('admin.tasks.form_due')}</label>
                                <input required type="date" name="due_date" value={taskFormData.due_date} onChange={handleTaskInput} min={todayDate} style={styles.input} />
                            </div>

                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '16px' }}>
                                <button type="button" onClick={() => setIsTaskModalOpen(false)} style={styles.cancelBtn}>{t('admin.tasks.btn_cancel')}</button>
                                <button type="submit" style={styles.primaryBtn}>{t('admin.tasks.btn_save')}</button>
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
    tabHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' },
    pageTitle: { margin: '0 0 24px 0', fontSize: '28px', color: '#0F172A', fontWeight: 'bold' },
    loadingText: { fontSize: '18px', color: '#64748B' },

    statsGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '24px' },
    statCard: { backgroundColor: '#FFFFFF', padding: '24px', borderRadius: '12px', border: '1px solid #E2E8F0', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' },
    statTitle: { margin: '0 0 16px 0', fontSize: '18px', color: '#0F172A' },
    statRow: { display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px dashed #E2E8F0', color: '#475569', fontSize: '15px' },

    tableCard: { backgroundColor: '#FFFFFF', borderRadius: '12px', border: '1px solid #E2E8F0', overflow: 'hidden' },
    table: { width: '100%', borderCollapse: 'collapse', textAlign: 'left' },
    th: { padding: '16px 24px', backgroundColor: '#F8FAFC', color: '#64748B', fontWeight: '600', fontSize: '14px', borderBottom: '1px solid #E2E8F0' },
    tr: { borderBottom: '1px solid #E2E8F0' },
    td: { padding: '16px 24px', color: '#334155', fontSize: '15px' },
    emptyTd: { padding: '32px', textAlign: 'center', color: '#94A3B8' },
    indexBadge: { padding: '4px 12px', borderRadius: '20px', fontWeight: 'bold', fontSize: '13px', whiteSpace: 'nowrap' },

    primaryBtn: { backgroundColor: '#3B82F6', color: 'white', padding: '10px 20px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer', transition: 'background-color 0.2s' },
    actionBtn: { backgroundColor: 'transparent', border: 'none', color: '#3B82F6', fontWeight: '600', cursor: 'pointer', padding: '4px 8px' },
    cancelBtn: { backgroundColor: '#F1F5F9', color: '#475569', padding: '10px 20px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer' },

    modalOverlay: { position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(15, 23, 42, 0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000 },
    modalContent: { backgroundColor: 'white', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '500px', boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1)' },
    inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
    label: { fontSize: '14px', fontWeight: '600', color: '#475569' },
    input: { padding: '10px 12px', borderRadius: '8px', border: '1px solid #CBD5E1', fontSize: '15px', outline: 'none' }
};

export default AdminPanel;