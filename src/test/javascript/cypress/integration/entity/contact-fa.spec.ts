import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Contact e2e test', () => {
  const contactPageUrl = '/contact-fa';
  const contactPageUrlPattern = new RegExp('/contact-fa(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contacts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contacts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contacts/*').as('deleteEntityRequest');
  });

  it('should load Contacts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Contact').should('exist');
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load details Contact page', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contact');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load create Contact page', () => {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should load edit Contact page', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should create an instance of Contact', () => {
    cy.visit(contactPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Contact');

    cy.get(`[data-cy="remoteId"]`).type('36134').should('have.value', '36134');

    cy.get(`[data-cy="number"]`).type('Vision-oriented parsing Global').should('have.value', 'Vision-oriented parsing Global');

    cy.get(`[data-cy="type"]`).select('COMPANY');

    cy.get(`[data-cy="gender"]`).select('OTHER');

    cy.get(`[data-cy="genderSalutationActive"]`).should('not.be.checked');
    cy.get(`[data-cy="genderSalutationActive"]`).click().should('be.checked');

    cy.get(`[data-cy="name"]`).type('application').should('have.value', 'application');

    cy.get(`[data-cy="nameAddition"]`).type('encryption').should('have.value', 'encryption');

    cy.get(`[data-cy="salutation"]`).type('system-worthy Health intuitive').should('have.value', 'system-worthy Health intuitive');

    cy.get(`[data-cy="phone"]`).type('(995) 782-7065 x48310').should('have.value', '(995) 782-7065 x48310');

    cy.get(`[data-cy="fax"]`).type('Mississippi Angola Bacon').should('have.value', 'Mississippi Angola Bacon');

    cy.get(`[data-cy="email"]`).type('Amir33@yahoo.com').should('have.value', 'Amir33@yahoo.com');

    cy.get(`[data-cy="website"]`).type('Steel Buckinghamshire Greens').should('have.value', 'Steel Buckinghamshire Greens');

    cy.get(`[data-cy="notes"]`).type('solution Designer').should('have.value', 'solution Designer');

    cy.get(`[data-cy="communicationLanguage"]`).type('Berkshire').should('have.value', 'Berkshire');

    cy.get(`[data-cy="communicationChannel"]`).select('BY_POST');

    cy.get(`[data-cy="communicationNewsletter"]`).select('SEND_TO_MAIN_CONTACT_ONLY');

    cy.get(`[data-cy="currency"]`).select('FKP');

    cy.get(`[data-cy="ebillAccountId"]`).type('Web Bangladesh').should('have.value', 'Web Bangladesh');

    cy.get(`[data-cy="vatIdentification"]`).type('panel Associate Shoes').should('have.value', 'panel Associate Shoes');

    cy.get(`[data-cy="vatRate"]`).type('79774').should('have.value', '79774');

    cy.get(`[data-cy="discountRate"]`).type('33411').should('have.value', '33411');

    cy.get(`[data-cy="discountType"]`).select('IN_PERCENT');

    cy.get(`[data-cy="paymentGrace"]`).type('57216').should('have.value', '57216');

    cy.get(`[data-cy="hourlyRate"]`).type('16197').should('have.value', '16197');

    cy.get(`[data-cy="created"]`).type('2021-08-16T22:31').should('have.value', '2021-08-16T22:31');

    cy.get(`[data-cy="mainAddressId"]`).type('81876').should('have.value', '81876');

    cy.get(`[data-cy="birthDate"]`).type('2021-08-16').should('have.value', '2021-08-16');

    cy.get(`[data-cy="birthPlace"]`).type('Analyst').should('have.value', 'Analyst');

    cy.get(`[data-cy="placeOfOrigin"]`).type('Awesome Sudan Shoes').should('have.value', 'Awesome Sudan Shoes');

    cy.get(`[data-cy="citizenCountry1"]`)
      .type('redefine Cambridgeshire Multi-channelled')
      .should('have.value', 'redefine Cambridgeshire Multi-channelled');

    cy.get(`[data-cy="citizenCountry2"]`).type('Pula').should('have.value', 'Pula');

    cy.get(`[data-cy="socialSecurityNumber"]`).type('Mobility Planner').should('have.value', 'Mobility Planner');

    cy.get(`[data-cy="hobbies"]`).type('Persistent').should('have.value', 'Persistent');

    cy.get(`[data-cy="dailyWork"]`).type('asynchronous').should('have.value', 'asynchronous');

    cy.get(`[data-cy="contactAttribute01"]`).type('web-enabled Soft Account').should('have.value', 'web-enabled Soft Account');

    cy.setFieldImageAsBytesOfEntity('avatar', 'integration-test.png', 'image/png');

    cy.get(`[data-cy="imageType"]`).type('Practical Supervisor Shoes').should('have.value', 'Practical Supervisor Shoes');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('relations');

    cy.setFieldSelectToLastOfEntity('groups');

    // since cypress clicks submit too fast before the blob fields are validated
    cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactPageUrlPattern);
  });

  it('should delete last instance of Contact', function () {
    cy.visit(contactPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contact').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
