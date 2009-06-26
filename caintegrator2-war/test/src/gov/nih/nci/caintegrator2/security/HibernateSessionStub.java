/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.security;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;

@SuppressWarnings("unchecked")
public class HibernateSessionStub implements Session {

    private static final long serialVersionUID = 1L;

    public SessionFactoryStub sessionFactory = new SessionFactoryStub();
    
    public Transaction beginTransaction() throws HibernateException {
        
        return null;
    }

    public void cancelQuery() throws HibernateException {
        

    }

    public void clear() {
        

    }

    public Connection close() throws HibernateException {
        
        return null;
    }

    public Connection connection() throws HibernateException {
        
        return null;
    }

    public boolean contains(Object object) {
        
        return false;
    }

    public Criteria createCriteria(Class persistentClass) {
        
        return null;
    }

    public Criteria createCriteria(String entityName) {
        
        return null;
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        
        return null;
    }

    public Criteria createCriteria(String entityName, String alias) {
        
        return null;
    }

    public Query createFilter(Object collection, String queryString) throws HibernateException {
        
        return null;
    }

    public Query createQuery(String queryString) throws HibernateException {
        
        return null;
    }

    public SQLQuery createSQLQuery(String queryString) throws HibernateException {
        
        return null;
    }

    public void delete(Object object) throws HibernateException {
        

    }

    public void delete(String entityName, Object object) throws HibernateException {
        

    }

    public void disableFilter(String filterName) {
        

    }

    public Connection disconnect() throws HibernateException {
        
        return null;
    }

    public Filter enableFilter(String filterName) {
        
        return null;
    }

    public void evict(Object object) throws HibernateException {
        

    }

    public void flush() throws HibernateException {
        

    }

    public Object get(Class clazz, Serializable id) throws HibernateException {
        
        return null;
    }

    public Object get(String entityName, Serializable id) throws HibernateException {
        
        return null;
    }

    public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
        
        return null;
    }

    public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        
        return null;
    }

    public CacheMode getCacheMode() {
        
        return null;
    }

    public LockMode getCurrentLockMode(Object object) throws HibernateException {
        
        return null;
    }

    public Filter getEnabledFilter(String filterName) {
        
        return null;
    }

    public EntityMode getEntityMode() {
        
        return null;
    }

    public String getEntityName(Object object) throws HibernateException {
        
        return null;
    }

    public FlushMode getFlushMode() {
        
        return null;
    }

    public Serializable getIdentifier(Object object) throws HibernateException {
        
        return null;
    }

    public Query getNamedQuery(String queryName) throws HibernateException {
        
        return null;
    }

    public Session getSession(EntityMode entityMode) {
        
        return null;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public SessionStatistics getStatistics() {
        
        return null;
    }

    public Transaction getTransaction() {
        
        return null;
    }

    public boolean isConnected() {
        
        return false;
    }

    public boolean isDirty() throws HibernateException {
        
        return false;
    }

    public boolean isOpen() {
        
        return false;
    }

    public Object load(Class theClass, Serializable id) throws HibernateException {
        
        return null;
    }

    public Object load(String entityName, Serializable id) throws HibernateException {
        
        return null;
    }

    public void load(Object object, Serializable id) throws HibernateException {
        

    }

    public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
        
        return null;
    }

    public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        
        return null;
    }

    public void lock(Object object, LockMode lockMode) throws HibernateException {
        

    }

    public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
        

    }

    public Object merge(Object object) throws HibernateException {
        
        return null;
    }

    public Object merge(String entityName, Object object) throws HibernateException {
        
        return null;
    }

    public void persist(Object object) throws HibernateException {
        

    }

    public void persist(String entityName, Object object) throws HibernateException {
        

    }

    public void reconnect() throws HibernateException {
        

    }

    public void reconnect(Connection connection) throws HibernateException {
        

    }

    public void refresh(Object object) throws HibernateException {
        

    }

    public void refresh(Object object, LockMode lockMode) throws HibernateException {
        

    }

    public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
        

    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
        

    }

    public Serializable save(Object object) throws HibernateException {
        
        return null;
    }

    public Serializable save(String entityName, Object object) throws HibernateException {
        
        return null;
    }

    public void saveOrUpdate(Object object) throws HibernateException {
        

    }

    public void saveOrUpdate(String entityName, Object object) throws HibernateException {
        

    }

    public void setCacheMode(CacheMode cacheMode) {
        

    }

    public void setFlushMode(FlushMode flushMode) {
        

    }

    public void setReadOnly(Object entity, boolean readOnly) {
        

    }

    public void update(Object object) throws HibernateException {
        

    }

    public void update(String entityName, Object object) throws HibernateException {
        

    }

}
