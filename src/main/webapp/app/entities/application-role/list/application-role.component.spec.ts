import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApplicationRoleService } from '../service/application-role.service';

import { ApplicationRoleComponent } from './application-role.component';

describe('Component Tests', () => {
  describe('ApplicationRole Management Component', () => {
    let comp: ApplicationRoleComponent;
    let fixture: ComponentFixture<ApplicationRoleComponent>;
    let service: ApplicationRoleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplicationRoleComponent],
      })
        .overrideTemplate(ApplicationRoleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationRoleComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ApplicationRoleService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.applicationRoles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
