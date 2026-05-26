import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';

const Animals = () => {
    const { t, i18n } = useTranslation();
    const { user } = useAuth();
    const navigate = useNavigate();

    const [animals, setAnimals] = useState([]);
    const [search, setSearch] = useState('');
    const [loading, setLoading] = useState(true);

    // Модальні вікна
    const [selectedAnimal, setSelectedAnimal] = useState(null);
    const [adoptionAnimal, setAdoptionAnimal] = useState(null);
    const [adoptionNote, setAdoptionNote] = useState('');

    useEffect(() => {
        axios.get('http://localhost:3000/api/animals')
            .then(res => {
                setAnimals(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching animals:", err);
                setLoading(false);
            });
    }, []);

    const formatDate = (dateString) => {
        if (!dateString) return '—';
        const date = new Date(dateString);
        return new Intl.DateTimeFormat(i18n.language, { year: 'numeric', month: 'long', day: 'numeric' }).format(date);
    };

    const renderValue = (value) => {
        return value && value.trim() !== '' ? value : '—';
    };

    const handleAdoptInit = (animal) => {
        if (!user) {
            alert(t('animals.login_required'));
            navigate('/login');
            return;
        }
        setAdoptionAnimal(animal);
    };

    const handleAdoptionSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:3000/api/adoptions', {
                animal_id: adoptionAnimal.id,
                user_id: user.id, // Автоматично беремо ID авторизованого юзера
                note: adoptionNote
            });

            alert(t('animals.adopt_success'));
            setAdoptionAnimal(null);
            setAdoptionNote('');
            setSelectedAnimal(null);
        } catch (err) {
            console.error("Adoption request failed:", err);
            alert(t('animals.adopt_error'));
        }
    };

    const filteredAnimals = animals
        .filter(animal =>
            (animal.name || '').toLowerCase().includes(search.toLowerCase()) ||
            (animal.breed || '').toLowerCase().includes(search.toLowerCase())
        )
        .sort((a, b) => (a.name || '').localeCompare(b.name || '', i18n.language));

    const getStatusStyle = (status) => {
        switch((status || '').toLowerCase()) {
            case 'available': return { backgroundColor: 'rgba(245, 117, 54, 0.1)', color: 'var(--active)' };
            case 'pending': return { backgroundColor: 'rgba(245, 158, 11, 0.1)', color: 'var(--rating)' };
            case 'adopted': return { backgroundColor: 'rgba(148, 163, 184, 0.1)', color: 'var(--grey)' };
            default: return { backgroundColor: 'rgba(148, 163, 184, 0.1)', color: 'var(--text-main)' };
        }
    };

    if (loading) return <div style={styles.centerText}>Loading...</div>;

    return (
        <div style={styles.container}>
            <div style={styles.headerRow}>
                <h2 style={styles.pageTitle}>{t('animals.title')}</h2>
                <input
                    type="text"
                    placeholder={t('animals.search_placeholder')}
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    style={styles.searchInput}
                />
            </div>

            {filteredAnimals.length === 0 ? (
                <p style={styles.centerText}>{t('animals.no_animals')}</p>
            ) : (
                <div style={styles.grid}>
                    {filteredAnimals.map(animal => (
                        <div key={animal.id} style={styles.card}>
                            <div>
                                <div style={styles.cardHeader}>
                                    <h3 style={styles.animalName}>{renderValue(animal.name)}</h3>
                                    <span style={{...styles.statusBadge, ...getStatusStyle(animal.status)}}>
                                        {t(`animals.status_${(animal.status || 'available').toLowerCase()}`)}
                                    </span>
                                </div>

                                <div style={styles.infoGrid}>
                                    <div style={styles.infoBlock}>
                                        <span style={styles.label}>{t('animals.species')}:</span>
                                        <span style={styles.value}>{renderValue(animal.species)}</span>
                                    </div>
                                    <div style={styles.infoBlock}>
                                        <span style={styles.label}>{t('animals.breed')}:</span>
                                        <span style={styles.value}>{renderValue(animal.breed)}</span>
                                    </div>
                                </div>

                                <div style={styles.descriptionBlock}>
                                    <span style={styles.label}>{t('animals.description')}:</span>
                                    <p style={styles.descriptionTextClamp}>{renderValue(animal.description)}</p>
                                </div>
                            </div>

                            <div style={styles.buttonGroup}>
                                <button
                                    style={styles.detailsButton}
                                    onClick={() => setSelectedAnimal(animal)}
                                >
                                    {t('animals.details_btn')}
                                </button>
                                {(animal.status || '').toLowerCase() === 'available' && (
                                    <button onClick={() => handleAdoptInit(animal)} style={styles.adoptButton}>
                                        {t('animals.adopt_btn')}
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {selectedAnimal && (
                <div style={styles.modalOverlay} onClick={() => setSelectedAnimal(null)}>
                    <div style={styles.modalContent} onClick={(e) => e.stopPropagation()}>
                        <div style={styles.modalHeader}>
                            <h2 style={styles.modalTitle}>{renderValue(selectedAnimal.name)}</h2>
                            <button style={styles.closeIcon} onClick={() => setSelectedAnimal(null)}>×</button>
                        </div>

                        <div style={styles.modalBody}>
                            <p><strong>{t('animals.species')}:</strong> {renderValue(selectedAnimal.species)}</p>
                            <p><strong>{t('animals.breed')}:</strong> {renderValue(selectedAnimal.breed)}</p>
                            <p><strong>{t('animals.gender')}:</strong> {(selectedAnimal.gender || '').toLowerCase() === 'male' ? t('animals.gender_male') : t('animals.gender_female')}</p>
                            <p><strong>{t('animals.birth_date')}:</strong> {formatDate(selectedAnimal.birth_date)}</p>

                            <hr style={styles.divider} />

                            <p><strong>{t('animals.description')}:</strong></p>
                            <p style={styles.fullDescription}>{renderValue(selectedAnimal.description)}</p>
                        </div>

                        <div style={styles.modalFooter}>
                            <button style={styles.closeButton} onClick={() => setSelectedAnimal(null)}>
                                {t('animals.close_btn')}
                            </button>
                            {(selectedAnimal.status || '').toLowerCase() === 'available' && (
                                <button onClick={() => handleAdoptInit(selectedAnimal)} style={styles.adoptButtonModal}>
                                    {t('animals.adopt_btn')}
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {adoptionAnimal && (
                <div style={styles.modalOverlay} onClick={() => setAdoptionAnimal(null)}>
                    <div style={styles.modalContent} onClick={(e) => e.stopPropagation()}>
                        <div style={styles.modalHeader}>
                            <h2 style={styles.modalTitle}>{t('animals.adopt_modal_title')} ({renderValue(adoptionAnimal.name)})</h2>
                            <button style={styles.closeIcon} onClick={() => setAdoptionAnimal(null)}>×</button>
                        </div>

                        <form onSubmit={handleAdoptionSubmit}>
                            <div style={styles.modalBody}>
                                <div style={styles.formGroup}>
                                    <label style={styles.formLabel}>{t('animals.adopt_note_label')}</label>
                                    <textarea
                                        required
                                        placeholder={t('animals.adopt_note_placeholder')}
                                        value={adoptionNote}
                                        onChange={(e) => setAdoptionNote(e.target.value)}
                                        style={styles.textarea}
                                    />
                                </div>
                            </div>

                            <div style={styles.modalFooter}>
                                <button type="button" style={styles.closeButton} onClick={() => setAdoptionAnimal(null)}>
                                    {t('animals.close_btn')}
                                </button>
                                <button type="submit" style={styles.adoptButtonModal}>
                                    {t('animals.adopt_confirm_btn')}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

const styles = {
    container: { padding: '40px 32px', backgroundColor: 'var(--bg-main)', minHeight: 'calc(100vh - 74px)', boxSizing: 'border-box' },
    headerRow: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px', flexWrap: 'wrap', gap: '16px' },
    pageTitle: { margin: 0, color: 'var(--text-main)', fontSize: '28px', fontWeight: '700' },
    searchInput: { padding: '12px 16px', borderRadius: '8px', border: '1px solid var(--grey)', width: '100%', maxWidth: '320px', fontSize: '15px', outline: 'none' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '24px' },
    card: { backgroundColor: 'var(--bg-card)', borderRadius: '16px', padding: '24px', border: '1px solid #F1ECE9', boxShadow: '0 4px 12px rgba(0,0,0,0.02)', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', minHeight: '320px' },
    cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', borderBottom: '2px solid var(--bg-main)', paddingBottom: '12px' },
    animalName: { margin: 0, fontSize: '22px', color: 'var(--active)', fontWeight: '700' },
    statusBadge: { padding: '6px 12px', borderRadius: '20px', fontSize: '13px', fontWeight: '600' },
    infoGrid: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '14px', marginBottom: '18px' },
    infoBlock: { display: 'flex', flexDirection: 'column', gap: '4px' },
    label: { fontSize: '13px', color: 'var(--grey)', fontWeight: '500' },
    value: { fontSize: '15px', color: 'var(--text-main)', fontWeight: '600' },
    descriptionBlock: { borderTop: '1px dashed #E2E8F0', paddingTop: '12px', marginBottom: '24px' },
    descriptionTextClamp: { margin: '4px 0 0 0', fontSize: '14px', color: 'var(--text-main)', lineHeight: '1.5', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' },
    buttonGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
    detailsButton: { backgroundColor: 'transparent', color: 'var(--active)', border: '1px solid var(--active)', padding: '10px', borderRadius: '8px', fontWeight: 'bold', fontSize: '14px', cursor: 'pointer', width: '100%', transition: 'all 0.2s' },
    adoptButton: { backgroundColor: 'var(--active)', color: 'white', border: 'none', padding: '10px', borderRadius: '8px', fontWeight: 'bold', fontSize: '14px', cursor: 'pointer', width: '100%' },
    centerText: { textAlign: 'center', padding: '40px', color: 'var(--grey)', fontSize: '18px' },
    modalOverlay: { position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, padding: '20px' },
    modalContent: { backgroundColor: 'var(--bg-main)', borderRadius: '16px', width: '100%', maxWidth: '500px', maxHeight: '90vh', display: 'flex', flexDirection: 'column', boxShadow: '0 10px 30px rgba(0,0,0,0.2)' },
    modalHeader: { padding: '20px 24px', borderBottom: '1px solid #E2E8F0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    modalTitle: { margin: 0, color: 'var(--active)', fontSize: '22px', fontWeight: '700' },
    closeIcon: { background: 'none', border: 'none', fontSize: '28px', color: 'var(--grey)', cursor: 'pointer', lineHeight: '1' },
    modalBody: { padding: '24px', overflowY: 'auto', color: 'var(--text-main)', fontSize: '15px', lineHeight: '1.6' },
    fullDescription: { whiteSpace: 'pre-wrap' },
    divider: { border: 'none', borderTop: '1px dashed #E2E8F0', margin: '16px 0' },
    modalFooter: { padding: '20px 24px', borderTop: '1px solid #E2E8F0', display: 'flex', justifyContent: 'flex-end', gap: '12px' },
    closeButton: { backgroundColor: 'transparent', color: 'var(--grey)', border: '1px solid var(--grey)', padding: '10px 20px', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' },
    adoptButtonModal: { backgroundColor: 'var(--active)', color: 'white', border: 'none', padding: '10px 20px', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' },

    formGroup: { display: 'flex', flexDirection: 'column', gap: '8px', width: '100%' },
    formLabel: { fontSize: '14px', color: 'var(--grey)', fontWeight: '600' },
    textarea: { padding: '12px', borderRadius: '8px', border: '1px solid var(--grey)', outline: 'none', fontSize: '15px', fontFamily: 'inherit', minHeight: '100px', resize: 'vertical', boxSizing: 'border-box', width: '100%' }
};

export default Animals;